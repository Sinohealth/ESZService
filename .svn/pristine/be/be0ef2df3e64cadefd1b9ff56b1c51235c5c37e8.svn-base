package com.sinohealth.eszservice.queue;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.CollectionUtils;

import com.sinohealth.eszservice.queue.entity.BaseMessage;
import com.sinohealth.eszservice.queue.entity.StringMessage;

public class RedisQueue<T> implements InitializingBean, DisposableBean {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private RedisTemplate<String, T> redisTemplate;
	private String key;
	// private int cap = Short.MAX_VALUE;// 最大阻塞的容量，超过容量将会导致清空旧数据
	private byte[] rawKey;
	private RedisConnectionFactory factory;
	private RedisConnection connection;// for blocking
	private BoundListOperations<String, T> listOperations;// noblocking

	private Lock lock = new ReentrantLock();// 基于底层IO阻塞考虑

	private QueueListener<T> listener;// 异步回调

	private ListenerThread listenerThread;

	private boolean isClosed;

	public void setListener(QueueListener<T> listener) {
		this.listener = listener;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() throws Exception {
		factory = redisTemplate.getConnectionFactory();
		connection = RedisConnectionUtils.getConnection(factory);
		RedisSerializer<String> serializer = (RedisSerializer<String>) redisTemplate
				.getKeySerializer();
		rawKey = serializer.serialize(key);
		listOperations = redisTemplate.boundListOps(key);
		if (listener != null) {
			logger.info("启动队列监听线程");
			listenerThread = new ListenerThread();
			listenerThread.setDaemon(true);
			listenerThread.start();
		}
	}

	/**
	 * blocking remove and get last item from queue:BRPOP
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T takeFromTail(int timeout) throws InterruptedException {
		lock.lockInterruptibly();
		try {
			List<byte[]> results = connection.bRPop(timeout, rawKey);
			if (CollectionUtils.isEmpty(results)) {
				return null;
			}
			return (T) redisTemplate.getValueSerializer().deserialize(
					results.get(1));
		} finally {
			lock.unlock();
		}
	}

	public T takeFromTail() throws InterruptedException {
		return takeFromHead(0);
	}

	/**
	 * 从队列的头，插入
	 */
	public void pushFromHead(T value) {
		listOperations.leftPush(value);
	}

	public void pushFromTail(T value) {
		listOperations.rightPush(value);
	}

	/**
	 * noblocking
	 * 
	 * @return null if no item in queue
	 */
	public T removeFromHead() {
		return listOperations.leftPop();
	}

	public T removeFromTail() {
		return listOperations.rightPop();
	}

	/**
	 * blocking remove and get first item from queue:BLPOP
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T takeFromHead(int timeout) throws InterruptedException {
		lock.lockInterruptibly();
		try {
			List<byte[]> results = connection.bLPop(timeout, rawKey);
			if (CollectionUtils.isEmpty(results)) {
				return null;
			}
			return (T) redisTemplate.getValueSerializer().deserialize(
					results.get(1));
		} finally {
			lock.unlock();
		}
	}

	public T takeFromHead() throws InterruptedException {
		return takeFromHead(0);
	}

	@Override
	public void destroy() throws Exception {
		if (isClosed) {
			return;
		}
		shutdown();
		RedisConnectionUtils.releaseConnection(connection, factory);
	}

	private void shutdown() {
		try {
			if (null != listenerThread) {
				listenerThread.interrupt();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class ListenerThread extends Thread {

		@Override
		public void run() {
			try {
				while (!listenerThread.isInterrupted()) {
					T value = null;
					try {
						value = takeFromHead();
					} catch (RedisConnectionFailureException e) {
						do {
							try {
								logger.warn("连接断开，30秒后重新尝试连接： {}",
										e.getMessage());
								Thread.sleep(30000);
								logger.info("重新连接...");
								connection = RedisConnectionUtils
										.getConnection(factory);
								logger.info("重新连接完成，connection：{}", connection);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						} while ((!listenerThread.isInterrupted())
								&& (null == connection));
					}
					// 逐个执行
					if (value != null) {
						try {
							listener.onMessage(value);
							// System.out.println("::" + value.toString());
						} catch (Exception e) {
							logger.info("处理队列失败：{}", e);
						}
					}
				}
			} catch (InterruptedException e) {
				logger.warn("线程中断：{}", e);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		@SuppressWarnings("unchecked")
		RedisQueue<BaseMessage> redisQueue = (RedisQueue<BaseMessage>) context
				.getBean("eszserviceQueue");
		StringMessage message1 = new StringMessage();
		message1.setText("test:app1");
		redisQueue.pushFromHead(message1);
		System.out.println("1、====");
		Thread.sleep(5000);
		System.out.println("2、====");
		StringMessage message2 = new StringMessage();
		message2.setText("test:app2");
		redisQueue.pushFromHead(message2);
		System.out.println("3、====");
		Thread.sleep(5000);
		// redisQueue.destroy();

		// System.out.println("4、====");
		// while (true) {
		// String mqStr = redisQueue.takeFromTail();
		// if (mqStr == null) {
		// System.out.println("没有了!");
		// break;
		// }
		// System.out.println(mqStr);
		// }

	}

}