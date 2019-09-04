package com.lkx.code.netty.chat.protocol;

import org.msgpack.annotation.Message;


/**
 * 自定义消息实体类
 *
 */
@Message
public class ChatMessage {
	
	private String addr;		//IP地址及端口
	private String cmd;		//命令类型[LOGIN]或者[SYSTEM]或者[LOGOUT]
	private long time;		//命令发送时间
	private int online;		//当前在线人数
	private String sender;  //发送人
	private String receiver;	//接收人
	private String content;	//消息内容
	private String terminal; //终端
	
	public ChatMessage(){}
	
	public ChatMessage(String cmd, long time, int online, String content){
		this.cmd = cmd;
		this.time = time;
		this.online = online;
		this.content = content;
		this.terminal = terminal;
	}

	public ChatMessage(String cmd, String terminal, long time, String sender){
		this.cmd = cmd;
		this.time = time;
		this.sender = sender;
		this.terminal = terminal;
	}


	public ChatMessage(String cmd, long time, String sender, String content){
		this.cmd = cmd;
		this.time = time;
		this.sender = sender;
		this.content = content;
		this.terminal = terminal;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	@Override
	public String toString() {
		return "ChatMessage{" +
				"addr='" + addr + '\'' +
				", cmd='" + cmd + '\'' +
				", time=" + time +
				", online=" + online +
				", sender='" + sender + '\'' +
				", receiver='" + receiver + '\'' +
				", content='" + content + '\'' +
				'}';
	}
}
