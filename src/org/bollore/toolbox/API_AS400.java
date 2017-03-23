package org.bollore.toolbox;

import com.ibm.as400.access.*;

public class API_AS400 {
	
	/* OpenAS400Connection: Opens an AS400 connection */
	public static AS400 OpenAS400Connection(String host, String login, String pwd)
	throws Exception {
		  return new AS400(host, login, pwd);
	}
	
	/* CloseAS400Connection: Closes an AS400 connection */
	public static void CloseAS400Connection(AS400 sys)
	throws Exception {
		sys.disconnectService(AS400.COMMAND);
	}
	
	/* OpenPGM: Run an AS400 command */
	/* This function was initially developped to open a PGM library */
	public static boolean OpenPGM(AS400 sys, String command)
	throws Exception {
		  // Ouverture des bibliothèques
		  CommandCall cmd = new CommandCall(sys);
	
		  boolean result = cmd.run(command);
		  AS400Message[] messageList_spmencl = cmd.getMessageList();
		  for (int i = 0; i < messageList_spmencl.length; i++) {
				System.err.println("CodeRetour : " + messageList_spmencl[i].getID()
							+ " text : " + messageList_spmencl[i].getText());
		  }
		  
		  return result;
	}
	
	
	/* XCCIF: Calls the XCCIF API */ 
	public static String XCCIF(AS400 sys, String program, String jxccif, String jxlcpc2, String jxlcfm2_fields1, String jxlcfm2_jlcr1, String jxlcfm2_jlcr2, String jxlcfm2_fields2)
	throws Exception {
	      ProgramParameter[] parmList = new ProgramParameter[3];
	      
	      AS400Text jxccifConverter = new AS400Text(256, sys);
	      byte[] jxccif_param = jxccifConverter.toBytes(jxccif);
	      parmList[0] = new ProgramParameter(jxccif_param, 256);
	
	      AS400Text jxlcpc2Converter = new AS400Text(768, sys);
	      byte[] jxlcpc2_param = jxlcpc2Converter.toBytes(jxlcpc2);
	      parmList[1] = new ProgramParameter(jxlcpc2_param);
	  
	      // Convert jxlcfm2 parameter to a byte array
	      // Convert the first block of fields
	      AS400Text jxlcfm2_fields1Converter = new AS400Text(874, sys);
	      byte[] jxlcfm2_fields1_bytes = jxlcfm2_fields1Converter.toBytes(jxlcfm2_fields1);
	      
	      // Convert the packed decimal fields
	      AS400Text nullConverter = new AS400Text(8, sys);
	      byte[] null_value = nullConverter.toBytes("        ");     
	      // 8 bytes
	      byte[] jxlcfm2_jlcr1_bytes = 
	    	  (jxlcfm2_jlcr1 == null || "".equals(jxlcfm2_jlcr1.trim())) ? 
	    	    null_value :
	            (new AS400PackedDecimal(15,2)).toBytes(Double.parseDouble(jxlcfm2_jlcr1.trim()));
	      // 8 bytes
	      byte[] jxlcfm2_jlcr2_bytes =     	  
	    	  (jxlcfm2_jlcr2 == null || "".equals(jxlcfm2_jlcr2.trim())) ? 
	    		null_value :
	            (new AS400PackedDecimal(15,2)).toBytes(Double.parseDouble(jxlcfm2_jlcr2.trim()));
	      
	      // Convert the second block of fields
	      AS400Text jxlcfm2_fields2Converter = new AS400Text(390, sys);
	      byte[] jxlcfm2_fields2_bytes = jxlcfm2_fields2Converter.toBytes(jxlcfm2_fields2);
	      
	      // Construct the byte array
	      byte[] jxlcfm2_param = new byte[1280];
	      int k = 0;
	      for (k = 0; k<874; k++) jxlcfm2_param[k] = jxlcfm2_fields1_bytes[k];
	      for (k = 0; k<8; k++) jxlcfm2_param[874 + k] = jxlcfm2_jlcr1_bytes[k];
	      for (k = 0; k<8; k++) jxlcfm2_param[882 + k] = jxlcfm2_jlcr2_bytes[k];
	      for (k = 0; k<390; k++) jxlcfm2_param[890 + k] = jxlcfm2_fields2_bytes[k];  
	      parmList[2] = new ProgramParameter(jxlcfm2_param);
	      
	      ProgramCall pgm = new ProgramCall(sys, program, parmList);
	      
	      String result = new String();
	      if (pgm.run() != true){
	            AS400Message[] messageList = pgm.getMessageList();
	            for (int i = 0; i < messageList.length; i++) {
	                  System.err.println(messageList[i].getText());
	            }
	      }
	      else{
	            AS400Text textConverter  = new AS400Text(256,sys);
	            String javaText = (String) textConverter.toObject(parmList[0].getOutputData());
	            result = javaText;                           
	    	  }
	      return result;
	}
	
	
	/* XCAIF: Calls the XCAIF API */
	public static String XCAIF(AS400 sys, String program, String jxcaif, String jxlgsa2_fields1, String jxlgsa2_jz1001, String jxlgsa2_jz1002, String jxlgsa2_jz103, String jxlgsa2_fields2)
	throws Exception {
	      ProgramParameter[] parmList = new ProgramParameter[2];
	      
	      // Convert jxcaif parameter to a byte array
	      AS400Text jxcaifConverter = new AS400Text(256, sys);
	      byte[] jxcaif_param = jxcaifConverter.toBytes(jxcaif);
	      parmList[0] = new ProgramParameter(jxcaif_param, 256);
	
	      // Convert jxlgsa2 parameter to a byte array
	      // Convert the first block of fields
	      AS400Text jxlgsa2_fields1Converter = new AS400Text(187, sys);
	      byte[] jxlgsa2_fields1_bytes = jxlgsa2_fields1Converter.toBytes(jxlgsa2_fields1);
	      
	      // Convert the packed decimal fields
	      AS400Text nullConverter = new AS400Text(6, sys);
	      byte[] null_value = nullConverter.toBytes("      ");     
	      // 6 bytes
	      byte[] jxlgsa2_jz1001_bytes = 
	    	  (jxlgsa2_jz1001 == null || "".equals(jxlgsa2_jz1001.trim())) ? 
	    	    null_value :
	            (new AS400PackedDecimal(10,0)).toBytes((double) Integer.parseInt(jxlgsa2_jz1001.trim()));
	      // 6 bytes
	      byte[] jxlgsa2_jz1002_bytes =     	  
	    	  (jxlgsa2_jz1002 == null || "".equals(jxlgsa2_jz1002.trim())) ? 
	    		null_value :
	            (new AS400PackedDecimal(10,0)).toBytes((double) Integer.parseInt(jxlgsa2_jz1002.trim()));
	      // 6 bytes
	      byte[] jxlgsa2_jz103_bytes =     	  
	    	  (jxlgsa2_jz103 == null || "".equals(jxlgsa2_jz103.trim())) ? 
	    		null_value :
	            (new AS400PackedDecimal(10,0)).toBytes((double) Integer.parseInt(jxlgsa2_jz103.trim()));
	      
	      // Convert the second block of fields
	      AS400Text jxlgsa2_fields2Converter = new AS400Text(307, sys);
	      byte[] jxlgsa2_fields2_bytes = jxlgsa2_fields2Converter.toBytes(jxlgsa2_fields2);
	      
	      // Construct the byte array
	      byte[] jxlgsa2_param = new byte[512];
	      int k = 0;
	      for (k = 0; k<187; k++) jxlgsa2_param[k] = jxlgsa2_fields1_bytes[k];
	      for (k = 0; k<6; k++) jxlgsa2_param[187 + k] = jxlgsa2_jz1001_bytes[k];
	      for (k = 0; k<6; k++) jxlgsa2_param[193 + k] = jxlgsa2_jz1002_bytes[k];
	      for (k = 0; k<6; k++) jxlgsa2_param[199 + k] = jxlgsa2_jz103_bytes[k];
	      for (k = 0; k<307; k++) jxlgsa2_param[205 + k] = jxlgsa2_fields2_bytes[k];
	      parmList[1] = new ProgramParameter(jxlgsa2_param);
	
	      ProgramCall pgm = new ProgramCall(sys, program, parmList);
	      
	      String result = new String();    
	      if (pgm.run() != true){
	    	  AS400Message[] messageList = pgm.getMessageList();
	            for (int i = 0; i < messageList.length; i++) {
	                  System.err.println(messageList[i].getText());
	            }
	      }
	      else{
	    	  AS400Text textConverter  = new AS400Text(256,sys);
	            String javaText = (String) textConverter.toObject(parmList[0].getOutputData());
	            result = javaText;                           
	    	  }
	      return result;
	}
	
	/* YVRFA: Calls the YVRFA API */
	public static String YVRFA(AS400 sys, String program, String jylcfa)
	throws Exception {
	    ProgramParameter[] parmList = new ProgramParameter[1];
	    
	    AS400Text jylcfaConverter = new AS400Text(256, sys);
	    byte[] jylcfa_param = jylcfaConverter.toBytes(jylcfa);
	    parmList[0] = new ProgramParameter(jylcfa_param, 256);
	   
	    ProgramCall pgm = new ProgramCall(sys, program, parmList);
	    
	    String result = new String();
	    if (pgm.run() != true){
	          AS400Message[] messageList = pgm.getMessageList();
	          for (int i = 0; i < messageList.length; i++) {
	                System.err.println(messageList[i].getText());
	          }
	    }
	    else{
	          AS400Text textConverter  = new AS400Text(256,sys);
	          String javaText = (String) textConverter.toObject(parmList[0].getOutputData());
	          result = javaText;                           
	  	}
	    return result;
	}
	
	
	/* YLCFA: Calls the YLCFA API */
	public static String YLCFA(AS400 sys, String program, String jylcfa)
	throws Exception {
	    ProgramParameter[] parmList = new ProgramParameter[1];
	    
	    AS400Text jylcfaConverter = new AS400Text(256, sys);
	    byte[] jylcfa_param = jylcfaConverter.toBytes(jylcfa);
	    parmList[0] = new ProgramParameter(jylcfa_param, 256);
	   
	    ProgramCall pgm = new ProgramCall(sys, program, parmList);
	    
	    String result = new String();
	    if (pgm.run() != true){
	          AS400Message[] messageList = pgm.getMessageList();
	          for (int i = 0; i < messageList.length; i++) {
	                System.err.println(messageList[i].getText());
	          }
	    }
	    else{
	          AS400Text textConverter  = new AS400Text(256,sys);
	          String javaText = (String) textConverter.toObject(parmList[0].getOutputData());
	          result = javaText;                           
	  	}
	    return result;
	}
	
	
	/* YMSTD: Calls the YMSTD API */
	public static String YMSTD(AS400 sys, String program, String jymstd)
	throws Exception {
	    ProgramParameter[] parmList = new ProgramParameter[1];
	    
	    AS400Text jymstdConverter = new AS400Text(855, sys);
	    byte[] jymstd_param = jymstdConverter.toBytes(jymstd);
	    parmList[0] = new ProgramParameter(jymstd_param, 855);
	   
	    ProgramCall pgm = new ProgramCall(sys, program, parmList);
	    
	    String result = new String();
	    
	    if (pgm.run() != true)
	    {
	    	AS400Message[] messageList = pgm.getMessageList();
	    	for (int i = 0; i < messageList.length; i++)
	    	{
	    		System.err.println(messageList[i].getText());
	        }
	    }
	    else
	    {
	          AS400Text textConverter  = new AS400Text(855, sys);
	          String javaText = (String) textConverter.toObject(parmList[0].getOutputData());
	          result = javaText;                           
	  	}
	    return result;
	}
	
	
	/* YSUPD: Calls the YSUPD API */
	public static String YSUPD(AS400 sys, String program, String jysupd)
	throws Exception {
	    ProgramParameter[] parmList = new ProgramParameter[1];
	    
	    AS400Text jysupdConverter = new AS400Text(855, sys);
	    byte[] jysupd_param = jysupdConverter.toBytes(jysupd);
	    parmList[0] = new ProgramParameter(jysupd_param, 855);
	   
	    ProgramCall pgm = new ProgramCall(sys, program, parmList);
	    
	    String result = new String();
	    
	    if (pgm.run() != true)
	    {
	    	AS400Message[] messageList = pgm.getMessageList();
	    	for (int i = 0; i < messageList.length; i++)
	    	{
	    		System.err.println(messageList[i].getText());
	        }
	    }
	    else
	    {
	          AS400Text textConverter  = new AS400Text(855, sys);
	          String javaText = (String) textConverter.toObject(parmList[0].getOutputData());
	          result = javaText;                           
	  	}
	    return result;
	}
	
	/* SPTAK : Calls the SPTAK1 API */
	public static String SPTAK1(AS400 sys, String program, String jxlcpc2, String jxlcfm2_1, String jxlcfm2_jlcr1, String jxlcfm2_jlcr2, String jxlcfm2_2, int timeOut)
	throws Exception {
	    
		ProgramParameter[] parmList = new ProgramParameter[2];
	    
	    
		// Convert jxlcpc2 parameter to a byte array
		AS400Text jxlcpc2Converter = new AS400Text(768, sys);
		byte[] jxlcpc2_param = jxlcpc2Converter.toBytes(jxlcpc2);
	    parmList[0] = new ProgramParameter(jxlcpc2_param, 768);
		
	    // Convert jxlcfm2 parameter to a byte array
		// Convert the first block of fields
		AS400Text jxlcfm2_1Converter = new AS400Text(874, sys);
		byte[] jxlcfm2_1_bytes = jxlcfm2_1Converter.toBytes(jxlcfm2_1);
		
		// Convert the packed decimal fields
		AS400Text nullConverter = new AS400Text(8, sys);
		byte[] null_value = nullConverter.toBytes("        ");
		// 8 bytes
		byte[] jxlcfm2_jlcr1_bytes = (jxlcfm2_jlcr1 == null || "".equals(jxlcfm2_jlcr1.trim())) ? 
			null_value : (new AS400PackedDecimal(15,2)).toBytes(Double.parseDouble(jxlcfm2_jlcr1.trim()));
	      // 8 bytes
		byte[] jxlcfm2_jlcr2_bytes = (jxlcfm2_jlcr2 == null || "".equals(jxlcfm2_jlcr2.trim())) ? 
			null_value : (new AS400PackedDecimal(15,2)).toBytes(Double.parseDouble(jxlcfm2_jlcr2.trim()));
		  
		// Convert the second block of fields
		AS400Text jxlcfm2_2Converter = new AS400Text(874, sys);
		byte[] jxlcfm2_2_bytes = jxlcfm2_2Converter.toBytes(jxlcfm2_2);
		
		// Construct the byte array
		byte[] jxlcfm2_param = new byte[1280];
		int k = 0;
		for (k = 0; k<874; k++) jxlcfm2_param[k] = jxlcfm2_1_bytes[k];
		for (k = 0; k<8; k++) jxlcfm2_param[874 + k] = jxlcfm2_jlcr1_bytes[k];
		for (k = 0; k<8; k++) jxlcfm2_param[882 + k] = jxlcfm2_jlcr2_bytes[k];
		for (k = 0; k<390; k++) jxlcfm2_param[890 + k] = jxlcfm2_2_bytes[k];  
		parmList[1] = new ProgramParameter(jxlcfm2_param);
		
		
		// Call program
	    ProgramCall pgm = new ProgramCall(sys, program, parmList);
	    pgm.setTimeOut(timeOut);
	    String result = new String();
	    
	    if (pgm.run() != true)
	    {
	    	AS400Message[] messageList = pgm.getMessageList();
	    	for (int i = 0; i < messageList.length; i++)
	    	{
	    		System.err.println(messageList[i].getText());
	        }
	    }
	    else
	    {
	          AS400Text textConverter  = new AS400Text(768, sys);
	          String javaText = (String) textConverter.toObject(parmList[0].getOutputData());
	          result = javaText;                           
	  	}
	    return result;
	}
	
	/* SPWMV1: Calls the SPWMV1 API */
	public static String SPWMV1(AS400 sys, String program)
	throws Exception {
	    
		ProgramParameter[] parmList = new ProgramParameter[1];
	    AS400Text jspwmv1Converter = new AS400Text(855, sys);
	    byte[] jspwmv1_param = jspwmv1Converter.toBytes("");
	    parmList[0] = new ProgramParameter(jspwmv1_param, 0);
	   
	    ProgramCall pgm = new ProgramCall(sys, program, null);  
	    String result 	= new String();
	    
	    if (pgm.run() != true)
	    {
	    	AS400Message[] messageList = pgm.getMessageList();
	    	
	    	for (int i = 0; i < messageList.length; i++)
	    		System.err.println(messageList[i].getText());
	    }
	    else
	    {
	          AS400Text textConverter  = new AS400Text(855, sys);
	          String javaText = (String) textConverter.toObject(parmList[0].getOutputData());
	          result = javaText;                           
	  	}
	    return result;
	}
	
	/* executeCmd : calls an AS400 function */
	public static String executeCmd(String host, String login, String pwd, String command) throws Exception {
		AS400 sys = new AS400(host, login, pwd);
		CommandCall cmd = new CommandCall(sys);
		cmd.run(command);
		String msg = "";

		AS400Message[] messageList = cmd.getMessageList();

		// Log en console
		for (int i = 0; i < messageList.length; i++) {
			msg += messageList[i].getID() + "#" + messageList[i].getText() + "#"; 
		}

		sys.disconnectService(AS400.COMMAND);
		return msg;
	}
	
	/* executeCmd2 : calls an AS400 procedure */
	public static void  executeCmd2(String host, String user, String password, String command) throws Exception {

		//AS400 sys = new AS400(host);
		AS400 sys = new AS400(host, user, password);

		CommandCall cmd = new CommandCall(sys);
		cmd.run(command);

		AS400Message[] messageList = cmd.getMessageList();

		// Log en console
		for (int i = 0; i < messageList.length; i++) {
			System.out.println("CodeRetour : " + messageList[i].getID()
					+ " text : " + messageList[i].getText());
		}

		sys.disconnectService(AS400.COMMAND);
	}
	
}
