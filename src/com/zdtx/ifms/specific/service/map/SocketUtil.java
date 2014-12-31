package com.zdtx.ifms.specific.service.map;


public class SocketUtil {
//	private static byte[] temptype ;
//	static{
//		temptype=new byte[1024];//读取socket流的缓存容器
//	}
//	public static String socketip=getproperty("socket.ip");
//	public static int socketport=Integer.valueOf(getproperty("socket.port")) ;
//	public static int sockettimeout=Integer.valueOf(getproperty("socket.time_out")) ;
//	public static int logintimeout=Integer.valueOf(getproperty("socket.login_time_out"));
//	public static String setgps=getproperty("socket.gps.isopen");
//	public static String socketgpsname=getproperty("socket.gps.username");
//	public static String socketgpspwd=getproperty("socket.gps.password");
//	public static String setsend=getproperty("socket.base.isopen");
//	public static String socketbasename=getproperty("socket.base.username");
//	public static String socketbasepwd=getproperty("socket.base.password");
//	public static String opencard=getproperty("socket.card.isopen");
//	public static int socketcardport=Integer.valueOf(getproperty("socket.card.port")) ;
//	private static final String IMAGE_PATH = System.getProperty("catalina.home")+ "/logss" ;
//	
//	public static void  savelog(String str){
//		File dir = new File(IMAGE_PATH);
//		if(!dir.isDirectory()){
//			dir.mkdir();
//		}
//		
//		File f=new File(IMAGE_PATH+"/logs.txt");
//		
//		if(!f.isFile()){
//			try {
//				f.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		PrintWriter pw=null;
//		 FileWriter fileWriter = null;  
//		try {
//			fileWriter=new FileWriter(f, true);
//			pw=new PrintWriter(fileWriter);
//			pw.println(str);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				fileWriter.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			pw.close();
//		
//		}
//		
//	
//	}
//	
//	
//	/***
//	 * 创建登陆信息请求字符串
//	 * 
//	 * @param username
//	 * @param password
//	 * @return $$|A0|system_admin|system_admin|##
//	 */
//	public static String loginStr(String username, String password) {
//		return "$$|A0|" + username + "|" + password + "|##";
//	}
//	/***
//	 * 创建订阅围栏信息字符串
//	 * 
//	 * @param username
//	 * @param password
//	 * @return $$|A0|system_admin|system_admin|##
//	 */
//	public static String fenceStr(String username) {
//		return "$$|A6|"+username+"|123456|1|1,|##";
//	}
//	
//	/***
//	 * 创建清除故障码
//	 * $$|AB|8|[车号]|[发送给OBD的内容]|##
//	 * $$|AB|8|955555|001430340D|##
//	 * @param vehname [车号]
//	 * @param obdStr  [发送给OBD的内容]
//	 * @return $$|A0|system_admin|system_admin|##
//	 */
//	public static String guzhangmaStr(String vehname, String obdStr) {
//		return "$$|AB|8|"+vehname+"|"+obdStr+"|##";
//	}
//	
//	
//	/***
//	 * 告警列表
//	 * $$|B5|[时间戳]|[车辆名称]|[设置参数列表]|##
//	 * @return
//	 */
//	public static String thresholdStr(String timec,String vehname,String setlist){
//		return "$$|B5|"+timec+"|"+vehname+"|"+setlist+"|##";
//	}
//	
//	/***
//	 * 创建告警列表参数
//	 * @param name
//	 * @param startvalue
//	 * @param endvalue
//	 * @param usetime
//	 * @return
//	 */
//	public static String setlist(Long typeid,Long startvalue,Long endvalue,Long usetime){
//		String str="";
//		
//		if(15==typeid){ //speeding
//			if(startvalue!=null){
//				str+="0055,"+startvalue+",;";
//			}
//			if(usetime!=null){
//				str+="00F0,"+usetime+",;";
//			}
//		}else if(11==typeid){//Sudden Acceleration
//			if(startvalue!=null){
//				str+="FC00,"+startvalue+",;";
//			}
//			if(usetime!=null){
//				str+="FC01,"+usetime+",;";
//			}
//		}else if(12==typeid){//Sudden Braking 
//			if(startvalue!=null){
//				str+="FC02,"+startvalue+",;";
//			}
//			if(usetime!=null){
//				str+="FC03,"+usetime+",;";
//			}
//		}else if(17==typeid){//Idle
//			if(usetime!=null){
//				str+="FC15,"+usetime+",;";
//			}
//		}else if(19==typeid){ //Engine Overspeed
//			if(startvalue!=null){
//				str+="FC17,"+startvalue+",;";
//			}
//			if(usetime!=null){
//				str+="00FD,"+usetime+",;";
//			}
//		}else if(13==typeid){ //Sudden left Sudden Right
//			if(startvalue!=null){
//				str+="FC19,"+startvalue+",;";
//			}
//			if(endvalue!=null){
//				str+="00FA,"+endvalue+",;";
//			}
//			if(usetime!=null){
//				str+="FC1A,"+usetime+",;";
//			}
//		}else if(16==typeid){ //speeding
//			if(startvalue!=null){
//				str+="FC21,"+startvalue+",;";
//			}
//			if(endvalue!=null){
//				str+="FC20,"+endvalue+",;";
//			}
//			if(usetime!=null){
//				str+="00F7,"+usetime+",;";
//			}
//		}
//		
//		return str;
//	}
//	
//	public static String setIPandPort(String ip,String port){
//		String str="0013,"+ip+",;0018,"+port+",;";
//		return str;
//	}
//	
//	/***
//	 * 维护用户表
//	 *  $$|60|ctx|123456|1|1|1|system_admin|##	  1删除
//	 *	$$|60|ctx|123456|1|1|0|system_admin|##    0新增
//	 */
//	public static String userStr( String newName,String flag) {
//		return "$$|60|"+socketbasename+"|123456|1|1|"+flag+"|"+newName+"|##";
//	}
//	
//	
//	/***
//	 * 维护车辆表
//	 *	$$|60|xjp10|123456|2|1|1|0001|##   1删除
//	 *	$$|60|xjp10|123456|2|1|0|1001|##   0新增
//	 */
//	public static String vehSocket( String newName,String flag) {
//		return "$$|60|"+socketbasename+"|123456|2|1|"+flag+"|"+newName+"|##";
//	}
//	/***
//	 * 8、断油断电指令
//		断油断电
//		$$|AA|1|[时间戳]|[用户名]|[车辆名称]|AA55|##
//		恢复供油供电
//		$$|AA|1|[时间戳]|[用户名]|[车辆名称]|1111|##
//	 * @param time[时间戳]
//	 * @param username[用户名]
//	 * @param vehname[车辆名称]
//	 * @param flag "1"断油断电AA55 "2"恢复供油供电1111
//	 * @return $$|A0|system_admin|system_admin|##
//	 */
//	public static String duanyouStr(String time, String username,String vehname,String flag) {
//		if(flag.equals("1")){
//			return "$$|AA|1|"+time+"|"+socketbasename+"|"+vehname+"|AA55|##";
//		}else{
//			return "$$|AA|1|"+time+"|"+socketbasename+"|"+vehname+"|1111|##";
//		}
//	}
//	/***
//	 * 发送socket
//	 * @param 登陆用户
//	 * @param 字符串
//	 */
//	public static boolean sendSocket( String str) {
//		String strtemp = BaseSocketServlet.sendsocket(str);
//		if (strtemp.equals("nosocket")) {
//			return false;
//		} else if (strtemp.equals("timeout")) {
//			return false;
//		} else {
//			String[] strary = getBustext(strtemp);
//			String str2 = strary[5];
//
//			/***
//			 * 业务应答：0,成功 1,车辆不存在 2,车辆不在线
//			 */
//			if (str2.startsWith("0")) {
//				return true;
//			} else if (str2.startsWith("1")) {
//				return false;
//			} else if (str2.startsWith("2")) {
//				return false;
//			}
//			return false;
//		}
//	}
//
//	/***
//	 * 解析登陆回馈信息
//	 * 
//	 * @param str
//	 * @return true 登陆成功 false 登陆失败
//	 */
//	public static boolean logincheck(String str) {
//		if (str == null || str.equals("")) {
//			return false;
//		}
//		String[] sary = str.split("\\|");
//		if (sary[4].equals("1")) {
//			return true;
//		}
//		return false;
//	}
//	/***
//	 * 解析登陆回馈信息
//	 * 
//	 * @param str
//	 * @return true 登陆成功 false 登陆失败
//	 */
//	public static boolean logincheck2(String str) {
//		if (str == null || str.equals("")) {
//			return false;
//		}
//		String[] sary = str.split("\\|");
//		if (sary[2].equals("ERROR")) {
//			return false;
//		}else{
//			
//			return true;
//		}
//	}
//
//	/***
//	 * 解析返回信息
//	 * 
//	 * @param str
//	 *            : 1.返回车辆数据
//	 *            $$|B6|[车辆所属]|[车辆名称]|[GPS数据产生日期]|[GPS数据产生时间]|[纬度]|[经度]
//	 *            |[速度]|[方位角]
//	 *            |[高度]|[车辆运行标识]|[下一个站站点序号]|[发动机温度]|[车厢内温度]|[行使里程]|[车上人数]
//	 *            |[已发车或已到达终点标识]
//	 *            |[已发车或已到达终点日期]|已发车或已到达终点时间]|[已发车或已到达终点方向]|[车辆告警状态]
//	 *            |[驾驶员ID]|[是否在线]
//	 *            |[离线原因]|[趟次]|[班次]|[原始的线路号]|[增加字段离线类型]|[驾驶员名称]|[司机在线]|##
//	 *            2.返回定位数据 $$|C2|[车辆所属]|[车辆名称]|[GPS数据产生日期]|[GPS数据产生时间]|[纬度]|[经度]
//	 *            |[速度]|[方位角]
//	 *            |[高度]|[车辆运行标识]|[下一个站站点序号]|[发动机温度]|[车厢内温度]|[行使里程]|[车上人数]
//	 *            |[已发车或已到达终点标识]
//	 *            |[已发车或已到达终点日期]|已发车或已到达终点时间]|[已发车或已到达终点方向]|[车辆告警状态]
//	 *            |[驾驶员ID]|[是否在线]
//	 *            |[离线原因]|[趟次]|[班次]|[原始的线路号]|[增加字段离线类型]|[驾驶员名称]|[司机在线]|##
//	 * @return
//	 */
//	public static String[] getBustext(String str) {
//		return str.split("\\|");
//	}
//
//	/***
//	 * 创建车辆名称请求字符串
//	 * 
//	 * @param busname
//	 * @return 例：$$|A1|110001|##
//	 */
//	public static String busnameStr(String busname) {
//		return "$$|A1|" + busname + "|##";
//	}
//
//	public static String getStringByInputStream(InputStream brin)
//			throws IOException {
//		if(brin.available()!=0){
//			int count=brin.available();
////			String strall="";
//			StringBuffer strall2=new StringBuffer("");
//			int z=brin.read(temptype,0,temptype.length);
//			while(z!=0 && z!=-1  ){
////				 strall += (new String(Arrays.copyOf(temptype, z)));
//				 strall2.append((new String(Arrays.copyOf(temptype, z),"GBK")));
//				 count=count-z;
//				 if(z<1024){
//					 break;
//				 }
//				 if(count<=0){
//					 break;
//				 }
//				 z=brin.read(temptype,0,temptype.length);
//			  }
////			return strall;
//			return strall2.toString();
//		}else{
//			return "";
//		}
//	}
//
//	/***
//	 * 取得gps字符串的数组
//	 * 
//	 * @param str
//	 * @return
//	 */
//	public static List<String[]> getGpsStrArr(String str) {
//		List<String[]> strary=new ArrayList<String[]>();
//		if (stringAllCheck(str)) {// 验证数据是否完整
////			if (testGpsStr(str)) {
////				return getBustext(str.substring(0, str.indexOf("##") + 2));
////			}
//			List<String> strs= subText(str);
//			if(strs!=null && strs.size()!=0){
//				for(String str1:strs){
//					strary.add(getBustext(str1));
//				}
//				return strary;
//			}
//		}
//		return null;
//	}
//
//	/***
//	 * 验证字符串是否是以$$开始 以 ##结尾
//	 * 
//	 * @param str
//	 * @return
//	 */
//	public static boolean stringAllCheck(String str) {
//		return str.startsWith("$$") && str.endsWith("##");
//	}
//
//	/***
//	 * 3 验证是否是gps数据
//	 * 
//	 * @param str
//	 * @return
//	 */
//	public static boolean testGpsStr(String str) {
//		return str.startsWith("$$|C2|");
//	}
//	/***
//	 * 分解返回
//	 * @param text
//	 * @return
//	 */
//	public static List<String> subText(String text){
//		List<String> texts=new ArrayList<String>();
//		
//		while(text.indexOf("$$")!=-1){
//			texts.add(text.substring(text.indexOf("$$"), text.indexOf("##")+2));
//			text=text.substring( text.indexOf("##")+2);
//		}
//		return texts;
//	}
//	public static String getproperty(String name){
//		InputStream inputStream = (new SocketUtil()).getClass().getClassLoader().getResourceAsStream("application.properties");
//		Properties p = new Properties();
//		try {
//			p.load(inputStream);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		return p.getProperty(name);
//	}
	
}
