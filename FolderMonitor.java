package com.octopus.monitor;

import java.util.ArrayList;
import java.util.List;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.octopus.monitor.lintener.JNotifyListenerAdapter;

public class FolderMonitor {

	private final Logger logger = LoggerFactory.getLogger(FolderMonitor.class);


	private ArrayList<Integer> watchIds = new ArrayList<Integer>();

	private List<JNotifyListenerAdapter> listeners;


	public List<JNotifyListenerAdapter> getListeners() {
		return listeners;
	}

	public void setListeners(List<JNotifyListenerAdapter> listeners) {
		this.listeners = listeners;
	}

	/**
	 * 初始化方法
	 */
	public void init(){
		logger.debug("初始化监听的文件夹...");
		logger.info("java.library.path : [{}]",System.getProperty("java.library.path"));

		System.loadLibrary("jnotify");

		int mask = JNotify.FILE_CREATED;
		boolean watchSub = false;

		try {

			if(null == listeners || listeners.isEmpty()){
				logger.info("没有需要监听的路径！");
			} else {
				for(JNotifyListenerAdapter listener : listeners){
					int watchId = JNotify.addWatch(listener.getMonitorPath(), mask, watchSub, listener);
					watchIds.add(watchId);
				}
			}

		} catch (JNotifyException e) {
			logger.error("文件夹监控异常！",e);
		}

		logger.debug("初始化监听的文件夹完成...");
	}

	/**
	 * 销毁方法
	 */
	public void destory() {
		try {
			for (int id : watchIds) {
				JNotify.removeWatch(id);
			}
		} catch (JNotifyException e) {
			logger.error("文件夹监控移除失败！",e);
		}

		logger.debug("文件夹监听器销毁成功...");
	}

}
