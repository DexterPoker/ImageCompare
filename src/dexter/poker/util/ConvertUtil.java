package dexter.poker.util;
/**
* @author DexterPoker
* @date 2017年1月11日-上午10:28:13
**/
public class ConvertUtil {

	public static int[] string2IntArray(String string){
		String str[] = string.split(",");  
		int array[] = new int[str.length];  
		for(int i=0;i<str.length;i++){  
		    array[i]=Integer.parseInt(str[i]);
		}
		return array;
	}
	
	public static String intArray2String(int[] array){
		String result = "";
		for(int i : array){
			result += i + ",";
		}
		return result.substring(0, result.length()-1);
	}
	
}
