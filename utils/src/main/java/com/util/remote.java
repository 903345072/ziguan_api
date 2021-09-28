package com.util;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;


public class remote
{
	public interface TdxLibrary extends Library 
	{
            //�����溯��
	    public  void OpenTdx();
	    public  void CloseTdx();
	    public  int Logon(String IP, short  Port, String Version, short  YybID,  String AccountNo, String TradeAccount, String JyPassword, String TxPassword, byte[] ErrInfo);
	    public  void Logoff(int ClientID);
	    public  void QueryData(int ClientID, int Category,  byte[] Result,  byte[] ErrInfo);
	    public  void SendOrder(int ClientID, int Category, int PriceType, String Gddm, String Zqdm, float Price, int Quantity, byte[] Result,  byte[] ErrInfo);
	    public  void CancelOrder(int ClientID, String ExchangeID, String hth,  byte[] Result,  byte[] ErrInfo);
	    public  void GetQuote(int ClientID, String Zqdm,  byte[] Result,  byte[] ErrInfo);
	    public  void Repay(int ClientID, String Amount, byte[] Result,  byte[] ErrInfo);
	        

            //��ͨ�����������ĺ���
            public  void QueryHistoryData(int ClientID, int Category, String StartDate, String EndDate, byte[] Result,  byte[] ErrInfo);
            public  void QueryDatas(int ClientID, int[] Category, int Count, Pointer[] Result, Pointer[] ErrInfo);
            public  void SendOrders(int ClientID, int[] Category, int[] PriceType, String[] Gddm, String[] Zqdm, float[] Price, int[] Quantity, int Count,Pointer[] Result, Pointer[] ErrInfo);
            public  void CancelOrders(int ClientID, String[] ExchangeID,  String[] hth, int Count, Pointer[] Result, Pointer[] ErrInfo);
            public  void GetQuotes(int ClientID, String[] Zqdm, int Count,Pointer[] Result, Pointer[] ErrInfo);


            //�߼������������ĺ���
            public  void QueryMultiAccountsDatas(int[] ClientID, int[] Category, int Count, Pointer[] Result, Pointer[] ErrInfo);
            public  void SendMultiAccountsOrders(int[] ClientID, int[] Category, int[] PriceType, String[] Gddm, String[] Zqdm, float[] Price, int[] Quantity, int Count,Pointer[] Result, Pointer[] ErrInfo);
            public  void CancelMultiAccountsOrders(int[] ClientID,  String[] ExchangeID, String[] hth, int Count, Pointer[] Result, Pointer[] ErrInfo);
            public  void GetMultiAccountsQuotes(int[] ClientID, String[] Zqdm, int Count,Pointer[] Result, Pointer[] ErrInfo);
	}
	
	public static void main(String[] args)
	{
		try
		{
			//DLL��32λ��,��˱���ʹ��jdk32λ����,���ܵ���DLL;
			//�����Trade.dll��4��DLL���Ƶ�java����Ŀ¼��;
			//java���̱���������� jna.jar, �� https://github.com/twall/jna ���� jna.jar
                        //������ʲô���Ա�̣���������ϸ�Ķ�VC���ڵĹ���DLL���������Ĺ��ܺͲ�������˵��������ϸ�Ķ���������������ʱ�侫�����ޣ�ˡ�����
			byte[] Result=new byte[1024*1024];
			byte[] ErrInfo=new byte[256];

			TdxLibrary TdxLibrary1 = (TdxLibrary)Native.loadLibrary("Trade",TdxLibrary.class);




			TdxLibrary1.OpenTdx();

			//��¼
			int ClientID=TdxLibrary1.Logon("wt.htsc.com.cn", (short)7708, "6.33", (short)0, "1111111111111111", "1111111111111111","11111", "111", ErrInfo);
                        //��¼�ڶ����ʺ�
                        //int ClientID2=TdxLibrary1.Logon("111.111.111.111", (short)7708, "5.33", (short)0, "2222222222", "2222222222","222222", "111", ErrInfo);
			if (ClientID==-1)
			{
				System.out.println(Native.toString(ErrInfo, "GBK"));
				return;
			}

			System.out.println("��¼�ɹ�");

			//��ѯ�ʽ�
			TdxLibrary1.QueryData(ClientID, 0, Result, ErrInfo);
			//TdxLibrary1.QueryData(ClientID2, 0, Result, ErrInfo);//�ڶ����ʺŲ�ѯ�ʽ�
			System.out.println(Native.toString(Result, "GBK"));

			//������ѯ
			Pointer[] Result1=new Pointer[2];
			Pointer[] ErrInfo1=new Pointer[2];
			for(int i=0;i<2;i++)
			{
				Result1[i]=new Memory(1024*1024);
				ErrInfo1[i]=new Memory(256);
			}


			int[] Category={0,1};
			TdxLibrary1.QueryDatas(ClientID, Category, 2, Result1, ErrInfo1);

			System.out.println(Result1[0].getString(0, "GBK"));
			System.out.println(Result1[1].getString(0, "GBK"));


			//ע��
			TdxLibrary1.Logoff(ClientID);


			TdxLibrary1.CloseTdx();

			System.out.println("end");

		}
		catch(Exception e)
		{

		}
	}

}