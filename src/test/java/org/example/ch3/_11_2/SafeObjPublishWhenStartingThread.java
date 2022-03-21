package org.example.ch3._11_2;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Aris
 * @ClassName: SafeObjPublishWhenStartingThread
 * @Description: 在启动工作者线程时实现对象安全发布范例
 * @Date: 2022/3/21
 **/
@Slf4j
public class SafeObjPublishWhenStartingThread {

	private final Map<String, String> objectState;

	public SafeObjPublishWhenStartingThread(Map<String, String> objectState) {
		this.objectState = objectState;
		// 不要在构造器中启动工作线程，以免this逸出
	}

	private void init() {
		new Thread() {
			@Override
			public void run() {
				String val = objectState.get("key");
				log.info(val);
			}
		}.start();
	}

	public static SafeObjPublishWhenStartingThread getInstance(Map<String, String> objectState) {
		SafeObjPublishWhenStartingThread instance = new SafeObjPublishWhenStartingThread(objectState);
		instance.init();
		return instance;
	}
}
