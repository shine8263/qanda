package com.binwang.qanda.sqlite;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;



import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class DBManager {
	
	private static String dbPath="/sdcard/Android/data/com.binwang.qanda/";
	private static String queryLog=null;
	private static String dbStr=null;
	public static String[] timu=null;
	public static String[] daan=null;
	public static SQLiteDatabase db;
	
	public static void createDB(){
		makeRootDirectory(dbPath);
		//makeFilePath(dbPath, "qanda.db") ;
		setDbStr();
		try {
			File dbFile=new File(dbPath+"qanda.db");
            if (!dbFile.exists()) {
                db=SQLiteDatabase.openOrCreateDatabase(dbFile, null);
                createTable(db);
                insert(db);
            }else if(dbFile.exists()){
            	db=SQLiteDatabase.openOrCreateDatabase(dbFile, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	}
	
	//生成文件夹的方法
    public static File makeRootDirectory(String filePath){
    	File file=null;
    	try{
    		file=new File(filePath);
    		if(!file.exists()){
    			file.mkdir();
    		}
    	}catch(Exception e){
    		Log.i("error", e+" ");
    	}
    	return file;
    }
    // 生成文件
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        //makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
    //在数据库中创建表的方法
    private static void createTable(SQLiteDatabase db){   
    	//创建表SQL语句   
    	//String stu_table="create table usertable(timu text,daan1 text,daan2 text,daan3 text,daan4 text,daan5 text)";   
    	//String stu_table="create table usertable(timu text,daan text)";   
    	//执行SQL语句   
    	//db.execSQL(stu_table);   
    	db.execSQL("create table practiceTable(timu text,daan text)");  
    	db.execSQL("create table collectTable(timu text,daan text)"); 
    	db.execSQL("create table examTable(timu text,daan text)"); 
    	db.execSQL("create table examresultTable(selected text,selectRight text)"); 
    	db.execSQL("create table examerrorTable(timu text,daan text,selected text)"); 
    	db.execSQL("create table errorTable(timu text,daan text)");
    	db.execSQL("create table historyTable(name text,curtime text,usetime text,score text)");
    }  
    //在数据库中插入数据的方法
    //private static void insert(SQLiteDatabase db){   
    	//插入数据SQL语句   
    	//String stu_sql="insert into usertable(timu,daan1,daan2,daan3,daan4,daan5) values('人的呼吸系统包括呼吸道和(  )','心','肝','肺','脾','c')"; 
    	//String stu_sql="insert into usertable(timu,daan) values('人的呼吸系统包括呼吸道和(  )','心|肝|肺|脾|c')";   
    	//执行SQL语句   
    	//db.execSQL(stu_sql);  
    	//db.execSQL("insert into usertable(timu,daan) values('我是1111111','d1111111','d1111111','d1111111','d1111111','d1111111','d1111111')");
    	//db.execSQL("insert into usertable(timu,daan) values('我是2222222','d2222222')");
    //}
    private static void setDbStr(){
    	dbStr="人的呼吸系统包括呼吸道和(      ){}A.心|B.肝|C.肺|D.脾|C{}呼吸系统的功能主要是(      ){}A.进行气体交换|B.进行水液代谢|C.呼出氧气，吸入二氧化碳|D.进行血液循环|A{}气管在4、5胸椎处分成(      ){}A.上下主支气管|B.左右主支气管|C.前后主支气管|D.大小主支气管|B{}喉不仅是呼吸的通道，也是(      ){}A.消化器官|B.循环通道|C.发音器官|D.分泌器官|C{}鼻腔附近含有空气的骨腔叫做(      ){}A.鼻道|B.鼻旁窦|C.鼻甲|D.鼻前庭|B{}两侧声带之间的裂隙称为(      ){}A.咽腔|B.腭腔|C.前庭裂|D.声门|D{}气管软骨，具有弹性，使管腔保持开放，这种作用主要表现为(      ){}A.呼吸作用|B.弹性作用|C.支架作用|D.固定作用|C{}支气管、血管、淋巴管、神经出入于肺的地方称为(      ){}A.纵隔|B.肺门|C.肺尖|D.肺底|B{}在组织学上，肺内支气管的各级分支及其终端的大量肺泡又称为(      ){}A.肺间质|B.肺实质|C.两者都对|D.两者都不对|B{}肺表面具有活性物质，可以保持肺泡表面张力，保证肺泡结构稳定性，主要由(      ){}A.肺间质细胞分泌|B.巨噬细胞分泌|C.Ⅰ型肺泡细胞分泌|D.Ⅱ型肺泡细胞分泌|D{}肺叶由于叶间裂的存在而被分割，一般是：(      ){}A.左二右三|B.左三右二|C.左三右三|D.左二右二|A{}肺的功能血管是(      ){}A.肺动脉和肺静脉|B.支气管动脉和静脉|C.冠状动脉|D.腹主动脉|A{}肺动脉发出的部位是(      ){}A.左心房|B.左心室|C.右心房|D.右心室|D{}机体与外界环境之间的气体交换过程叫(      ){}A.呼吸|B.吐纳|C.换气|D.新陈代谢|A{}肺与外界环境之间的气体交换叫(      ){}A.呼吸|B.吐纳|C.肺通气|D.新陈代谢|C{}肺泡与肺毛细血管血液之间的气体交换叫(      ){}A.呼吸|B.肺换气|C.肺通气|D.新陈代谢|B{}组织毛细血管血液与组织细胞之间的气体交换叫(      ){}A.呼吸|B.肺换气|C.组织换气|D.新陈代谢|C{}呼吸的基本中枢位于(      ){}A.间脑|B.中脑|C.小脑|D.延髓|D{}调节呼吸的最重要的生理性化学因素是(      ){}A.CO2.|B.CO|C.NO2.|D.NO|A{}当CO2浓度轻度升高时，可以导致(      ){}A.暂停呼吸|B.呼吸抑制|C.呼吸兴奋|D.以上均不对|C{}轻度低氧可以导致(      ){}A.暂停呼吸|B.呼吸抑制|C.呼吸兴奋|D.以上均不对|C{}心脏在人体位于(      ){}A.前纵隔|B.中纵隔|C.后纵隔|D.上纵隔|B{}心脏一共有多少个腔？(      ){}A.1|B.2|C.3|D.4|D{}左房室瓣膜又叫(      ){}A.一尖瓣|B.二尖瓣|C.三尖瓣|D.四尖瓣|B{}右房室瓣膜又叫(      ){}A.一尖瓣|B.二尖瓣|C.三尖瓣|D.四尖瓣|C{}心脏瓣膜的主要功能是(      ){}A.防止血液循环|B.防止血液流过|C.防止血液倒流|D.防止血流过快|C{}心脏本身的营养血管是(      ){}A.微血管|B.冠状血管|C.肺动脉|D.主动脉|B{}心传导系由特殊分化的心肌细胞构成，主要(      ){}A.收缩心肌|B.舒张心肌|C.产生新心肌细胞|D.产生和传导冲动|D{}血液循环主要由哪两部分构成？(      ){}A.心脏和淋巴管|B.血管和淋巴管|C.心脏和血管|D.动脉和静脉|C{}体循环起始于(      ){}A.左心房|B.左心室|C.右心房|D.右心室|B{}肺循环起于(      ){}A.左心房|B.左心室|C.右心房|D.右心室|D{}冠脉循环的特点是(      ){}A.血压低、流速慢、血流量大小、摄氧率高|B.血压高、流速快、血流量大、摄氧率高高|C.血压高、流速快、血流量大、摄氧率低|D.血压低、流速慢、血流量小、摄氧率低|B{}当冠状动脉突然阻塞时，不易很快建立侧支循环，常可导致(      ){}A.心跳骤停|B.心律失常|C.心肌梗塞|D.心跳减慢|C{}微循环是指循环系统中(      ){}A.小动脉与小静脉部分|B.动静短路部分|C.微动脉与微静脉部分|D.微动脉与小动脉部分|C{}微循环的作用是(      ){}A.实现血液的物质运输|B.实现血液和组织之间的物质交换输|C.实现血液和淋巴之间的物质交换|D.实现淋巴和组织之间的物质运输|B{}血管内的血液对单位面积血管壁的侧压力叫(      ){}A.血阻|B.血压|C.血流变|D.血常规|B{}测血压前前须休息片刻，坐位或卧位，伸直肘部，手掌向上，应使(      ){}A.肱动脉与心脏在同一水平面|B.肱动脉与主动脉在同一水平面输|C.挠动脉心脏在同一水平面|D.肱静脉与心脏在同一水平面|A{}人体在不同的生理状态下，各器官组织的代谢水平不同，因而(      ){}A.对组织液的需求量不同|B.对血液的需求量不同|C.对淋巴液的需求量不同|D.对津液的需求量不同|B{}对冠脉血流量影响很大的因素是(      ){}A.血管因素|B.动脉血压|C.心肌节律性舒缩活动|D.血细胞|C{}体循环起始于左心室，终于(      ){}A.左心房|B.左心室|C.右心房|D.右心室|C{}循环系统包括(      ){}A.心血管系统和淋巴系统|B.心脏和血管|C.血管和淋巴管|D.心脏和淋巴管|A{}从动脉到静脉，血压逐渐(      ){}A.升高|B.波动增加|C.降低|D.以上均不对|C{}循环系统平均充盈压是指(      ){}A.动脉收缩压|B.动脉舒张压|C.血液停止流动时对血管壁的侧压力|D.以上均不对|C{}测血压时，放开血压计气门，当听诊器里搏动声突然变低或消失时，所指刻度是(      ){}A.收缩压|B.舒张压|C.平均动脉压|D.以上均不对|B{}正常人的心律是由何发起并控制的？(      ){}A.窦房结|B.房室结|C.浦肯野纤维|D.以上均不对|A{}一般来说，起到弹性贮器作用的是(      ){}A.大静脉|B.大动脉|C.毛细血管|D.小动脉|B{}肺静脉里的血液属于(      ){}A.静脉血|B.动脉血|C.两者都对|D.两者都不对|B{}经过体循环之后，最后流入右心房的血液属于(      ){}A.静脉血|B.动脉血|C.两者都对|D.两者都不对|A{}测血压时，袖带太紧可导致测得的血压(      ){}A.偏高|B.偏低|C.血压波动|D.以上均不对|B{}机体调节心血管的活动，主要通过何种机制？(      ){}A.神经调节|B.免疫调节|C.神经-体液调节|D.内分泌|C{}消化系统主要由消化管以及何种器官构成？(      ){}A.消化道|B.消化液|C.消化腺|D.消化酶|C{}食物在消化道内被分解为小分子物质的过程叫(      ){}A.分解|B.消化|C.排遗|D.吸收|B{}食物消化后，可吸收成分透过消化道粘膜进入血液或淋巴液的过程叫(      ){}A.分解|B.消化|C.排遗|D.吸收|D{}按解剖位置，咽可分为鼻咽部、口咽部以及(      ){}A.舌咽部|B.喉咽部|C.腭咽部|D.以上均不对|B{}食道位于喉咙与胸腔的后面，是连结咽喉到何种器官的肌肉管道？(      ){}A.胃|B.十二指肠|C.大肠|D.肛门|A{}食管下段括约肌的英文简写名称是(      ){}A.ELS|B.SLE|C.LES|D.SEL|C{}食管下段括约肌的主要生理功能是(      ){}A.加快食物推送|B.防止胃内容物逆流入食管|C.减慢食物推送|D.防止胆汁返流|B{}上下消化道分解的标志是(      ){}A.十二指肠|B.幽门窦|C.胰管开口|D.Treitz韧带|D{}食糜由胃排入十二指肠的过程叫(      ){}A.胃排空|B.消化|C.吸收|D.排遗|A{}进食时反射性引起胃壁平滑肌的舒张叫做胃的(      ){}A.反射性舒张|B.容受性舒张|C.紧张性收缩|D.蠕动|B{}胃壁组织由外而内可分为四层，则外面第二层应为(      ){}A.肌层|B.粘膜下层|C.浆膜层|D.粘膜层|A{}胃可分为五个区域，以下哪个区域离十二指肠最近？(      ){}A.贲门|B.胃底|C.幽门|D.胃体|C{}幽门括约肌和幽门瓣具有的作用是(      ){}A.加强胃的蠕动及容受性舒张|B.保证胃的紧张性性收缩|C.控制胃内容物排入十二指肠以及防止肠内容物逆流回胃|D.控制十二指肠液的分泌速率|C{}小肠特有的运动形式是(      ){}A.紧张性收缩|B.分节运动|C.蠕动|D.袋状运动|B{}阑尾是附着于何种器官之上的？(      ){}A.空肠|B.回肠|C.盲肠|D.十二指肠|C{}人体最大的消化腺器官是(      ){}A.胃|B.十二指肠|C.肝|D.脾|C{}以下哪项不是肝脏的生理功能？(      ){}A.代谢的枢纽|B.解毒|C.分泌胆汁|D.产生抗体|D{}左右肝管合并后称为(      ){}A.胆总管|B.肝总管|C.胆囊管|D.主胰管|B{}胆汁由何种细胞分泌？(      ){}A.胆细胞|B.肝细胞|C.上皮细胞|D.间质细胞|B{}下列哪项不是胆汁的生理功能？(      ){}A.乳化脂肪|B.中和胃酸|C.刺激肠蠕动|D.调节情绪|D{}胆囊的生理功能不包括(      ){}A.产生胆汁|B.贮存胆汁|C.浓缩胆汁|D.输送胆汁|A{}正常胆汁的分泌、排放特点是(      ){}A.间歇分泌、持续排放|B.持续分泌、间歇排放|C.持续分泌排放|D.间歇分泌排放|B{}胆盐进入小肠后，90%以上被回肠末端粘膜吸收，通过门静脉又回到肝脏，再成为合成胆汁的原料，然后胆汁又分泌入肠，该过程称为(      ){}A.回收利用|B.肠肝循环|C.门脉循环|D.肝胆循环|B{}胆盐对胆汁分泌的作用是(      ){}A.促进胆汁分泌|B.减慢胆汁的分泌|C.促进胆汁合成|D.以上均不对|A{}神经系统的基本结构和功能单位是(      ){}A.神经胶质|B.神经元|C.神经纤维|D.神经轴突|B{}神经元代谢与营养的中心是(      ){}A.尼氏体|B.树突|C.轴突|D.胞体|D{}神经组织由神经元和何种细胞构成？(      ){}A.上皮细胞|B.杯状细胞|C.神经胶质细胞|D.巨噬细胞|C{}将冲动由胞体传向外周的功能主要由何种结构承担？(      ){}A.髓鞘|B.胞体|C.树突|D.轴突|D{}根据神经元的功能，可分为感觉神经元、运动神经元以及(      ){}A.单极神经元|B.联络神经元|C.双极神经元|D.以上均不对|B{}神经纤维的结构有轴突以及(      ){}A.髓鞘|B.胞体|C.树突|D.轴突|A{}下列哪项不是神经胶质细胞的功能？(      ){}A.支持作用|B.修复和再生作用|C.绝缘和屏蔽作用|D.产生兴奋|D{}神经元间联系方式是互相接触，而不是细胞质的互相沟通该接触部位的结构特化称为(      ){}A.突触|B.触突|C.树突|D.以上均不对|A{}突触的基本结构不包括(      ){}A.突触前膜|B.突触间隙|C.突触后膜|D.以上均不对|D{}指细胞膜或细胞内能与某些化学物质发生特异性结合并诱发生物效应的特殊生物分子称为(      ){}A.抗原|B.神经递质|C.受体|D.调质|B{}中枢神经系统除了脑之外，还包括(      ){}A.脊柱|B.脊髓|C.神经节|D.以上均不对|B{}以下哪项不属于脑干结构？(      ){}A.中脑|B.脑桥|C.延髓|D.小脑|D{}在中枢神经系统里，神经细胞胞体集中的部位又称为(      ){}A.白质|B.灰质|C.黑质|D.纹状体|B{}大脑由左、右大脑半球构成，连接两半球的是(      ){}A.基底节|B.纹状体|C.胼胝体|D.漏斗结节|C{}调整肌紧张，维持身体平衡，控制肌肉的张力和协调主要由哪部分脑结构承担？(      ){}A.脑干|B.脊髓|C.小脑|D.延髓|C{}维持个体生命，包括心跳、呼吸、消化、体温、睡眠等重要生理功能的中枢系统是(      ){}A.脑干|B.脊髓|C.小脑|D.端脑|A{}人体共有多少对颅神经？(      ){}A.11|B.12|C.7|D.8|B{}反射活动的结构基础称为(      ){}A.反射带|B.条件反射|C.反射弧|D.以上均不对|C{}许多简单反射活动的低级中枢位于(      ){}A.大脑|B.脊髓|C.肢体|D.以上均不对|B{}在中枢神经系统里，不含胞体只有神经纤维聚集的部分称为(      ){}A.白质|B.灰质|C.黑质|D.纹状体|A{}神经元的基本功能除了感受刺激外，还有(      ){}A.营养功能|B.支持功能|C.传导兴奋|D.吞噬功能|C{}从传导速度来看，直径粗的神经纤维较细的(      ){}A.传导更远|B.传导更快|C.传导更慢|D.无影响|B{}从传导速度来看，有髓的神经纤维较无髓的(      ){}A.传导更远|B.传导更快|C.传导更慢|D.传导更近|B{}关于神经胶质细胞特性，哪项叙述错误？(      ){}A.数量多：10-50倍于神经元细胞|B.分布广：星型、少突、小胶质、施旺细胞、卫星细胞等|C.无化学突触有缝隙连接|D.可产生动作电位|D{}神经细胞静息时，递质在胞体一般贮存于何处？(      ){}A.线粒体|B.突触小泡|C.高尔基体|D.溶酶体|B{}神经细胞冲动到达时，神经递质可释放入何处：(      ){}A.突触前膜|B.突触后膜|C.突触间隙|D.以上均不对|C{}大脑表面的突起及凹陷称为(      ){}A.大脑半球|B.脑叶|C.脑岛|D.大脑沟回|D{}下列哪项不是脑脊液的功能？(      ){}A.缓冲|B.保护|C.运输代谢产物|D.吞噬|D{}周围神经与脑之间的通路是(      ){}A.脑干|B.小脑|C.神经节|D.脊髓|D{}处于调节和指挥人体生理活动中的主导地位是(      ){}A.免疫系统|B.代谢系统|C.神经系统|D.内分泌系统|C";
    	//dbStr="人的呼吸系统包括呼吸道和(      ){}A.心|B.肝|C.肺|D.脾|C{}呼吸系统的功能主要是(      ){}A.进行气体交换|B.进行水液代谢|C.呼出氧气，吸入二氧化碳|D.进行血液循环|A{}气管在4、5胸椎处分成(      ){}A.上下主支气管|B.左右主支气管|C.前后主支气管|D.大小主支气管|B";
    	//dbStr="人的呼吸系统包括呼吸道和(      ){}A.心|B.肝|C.肺|D.脾|C";
    }
    //数据库字符串裁剪
    private static void dbStrHandle(){
    	String[] strn=dbStr.split("\\{\\}");
    	timu=new String[strn.length/2];
    	daan=new String[strn.length/2];
    	for(int i=0;i<strn.length;i=i+2){
    		int n=(i+2)/2-1;//设置n为第几-1道题
    		timu[n]=strn[i];
    		daan[n]=strn[i+1];
    		}
    }
    
    //增加数据库数据
    private static void insert(SQLiteDatabase db){
    	String[] strn=dbStr.split("\\{\\}");
    	timu=new String[strn.length/2];
    	daan=new String[strn.length/2];
    	ContentValues cValue=new ContentValues();
    	for(int i=0;i<strn.length;i=i+2){
    		int n=(i+2)/2-1;//设置n为第几-1道题
    		timu[n]=strn[i];
    		daan[n]=strn[i+1];
    		cValue.put("timu",timu[n]);
    		cValue.put("daan", daan[n]);
    		db.insert("practiceTable",null,cValue);
    	}
    	//db.execSQL("insert into collectTable(timu,daan) values('空','A.空|B.空|C.空|D.空|空')");
    	//db.execSQL("insert into errorTable(timu,daan) values('空','A.空|B.空|C.空|D.空|空')");
    	//db.execSQL("insert into usertable(timu,daan) values('人的呼吸系统包括呼吸道和(      )','A.心|B.肝|C.肺|D.脾|C')");
    }
    
    //往模拟试题题库examTable里注入随机的20道题
    public static void insertExamTable(){
    	//先把examTable表里的数据擦掉
    	Cursor cursorX=null;
    	try{
    		cursorX=DBManager.db.query("examTable", null, null, null, null, null, null);
    		if(cursorX.getCount()!=0){
    			DBManager.db.delete("examTable", null, null);
    			DBManager.db.delete("examresultTable", null, null);
    			DBManager.db.delete("examerrorTable", null, null);
    		}else{}
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		if(cursorX!=null){
    			cursorX.close();
    		}
    	}
    	//往examTable表里塞20道题
    	Cursor cursor=null;
		try{
		cursor =DBManager.db.query("practiceTable",null,null,null,null,null,null);
		cursor.moveToFirst();
		int[] randomX=creatRandom(20,0,cursor.getCount());
		for(int i=0;i<20;i++){
			cursor.moveToPosition(randomX[i]);
	    	ContentValues cValue=new ContentValues();
	    	cValue.put("timu",cursor.getString(0));
    		cValue.put("daan", cursor.getString(1));
    		db.insert("examTable",null,cValue);
		}
		}catch(Exception e){
		    e.printStackTrace();
		}finally{
		    if(cursor != null){
		        cursor.close();
		    }
		}
    }
    
    public static void insertExamresultTable(String[] selected,String[] selectRight){
    	for(int i=0;i<selected.length;i++){
    		ContentValues cValue=new ContentValues();
    		cValue.put("selected", selected[i]);
    		cValue.put("selectRight", selectRight[i]);
    		DBManager.db.insert("examresultTable", null, cValue);
    	}
    }
    
    
  //产生size个不同的数，最小值为randomMin，最大值为randomMax-1
  	private static int[] creatRandom(int size,int randomMin,int randomMax){
      	int[] random=new int[size];
      	List<Integer> list=new ArrayList<Integer>();
      	for (int i=randomMin;i<randomMax;i++){
      		list.add(i);
      	}
      	for(int j=0;j<random.length;j++){
      		int index=(int)(Math.random()*(list.size()));
      		random[j]=list.get(index);
      		list.remove(index);
      	}
      	return random;
      }
    
    //查询数据库中数据的方法
    private static void query(SQLiteDatabase db) {   
    	//查询获得游标   
    	Cursor cursor = db.query("usertable",null,null,null,null,null,null);   
    	  
    	//判断游标是否为空   
    	if(cursor.moveToFirst()) {   
    	//遍历游标   
    	for(int i=0;i<cursor.getCount();i++){   
    	cursor.moveToPosition(i);   
    	//获得ID   
    	//int id = cursor.getInt(0);   
    	//获得用户名   
    	String username=cursor.getString(0);   
    	//获得密码   
    	String password=cursor.getString(1);   
    	//输出用户信息 System.out.println(id+":"+sname+":"+snumber);   
    	//System.out.println(username+":"+password);
    	queryLog=username+":"+password;
    	}   
    	}   
    	}  
}
