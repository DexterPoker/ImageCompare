package dexter.poker.util;

import java.util.Arrays;

/**
* @author DexterPoker
* @date 2017年1月9日-下午3:11:00
**/
public class TimerUtil {
	
	private String[] items;
	private long[] times;
	public String[] getItems() {
		return items;
	}
	public void setItems(String[] items) {
		this.items = items;
	}
	public long[] getTimes() {
		return times;
	}
	public void setTimes(long[] times) {
		this.times = times;
	}
	
	public TimerUtil(){
		this.items = new String[0];
		this.times = new long[0];
	}
	
	public void add(String title){
		items = Arrays.copyOf(items, items.length+1);
		times = Arrays.copyOf(times, times.length+1);
		items[items.length-1] = title;
		times[times.length-1] = System.currentTimeMillis();
	}
	
	public void finish(){
		items = Arrays.copyOf(items, items.length+1);
		times = Arrays.copyOf(times, times.length+1);
		items[items.length-1] = "end";
		times[times.length-1] = System.currentTimeMillis();
	}
	
	public String printAll(){
		StringBuilder sb = new StringBuilder("耗时统计:");
		if(items.length<2)
			return sb.append("失败，timer内统计项小于2项").toString();
		for(int i = 1 ;i < items.length ; i++){
			sb.append("     " + items[i-1] + ":" + (times[i] - times[i-1] + "ms"));
		}
		return sb.toString();
	}
	
	public String printLastItem(){
		if(items.length<2)
			return "timer内统计项小于2项";
		StringBuilder sb = new StringBuilder();
		sb.append(items[items.length-2] + "耗时:" + (times[times.length-1] - times[times.length-2] + "ms"));
		return sb.toString();
	}
}
