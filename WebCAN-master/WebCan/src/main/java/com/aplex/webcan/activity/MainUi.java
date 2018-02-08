package com.aplex.webcan.activity;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.android.socketcan.CanUtils;
import com.aplex.webcan.R;
import com.aplex.webcan.bean.CmdBean;
import com.aplex.webcan.bean.Constant;
import com.aplex.webcan.customview.LoadingDialog;
import com.aplex.webcan.fragment.ContentFragment;
import com.aplex.webcan.fragment.LeftFragment;
import com.aplex.webcan.slidingmenu.SlidingMenu;
import com.aplex.webcan.slidingmenu.app.SlidingActivity;
import com.aplex.webcan.util.UIUtils;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

public class MainUi extends SlidingActivity {
	private final static String TAG_MENU = "Left_fragment";
	private static final String TAG_CONTENT = "content_fragment";

	private LoadingDialog mLoadingDialog;
	
	private EMMessageListener msgListener;
	
	private byte ctl_Flag = 0;
	private byte led8_Flag = 0;
	private byte led9_Flag = 0;
	private byte led10_Flag = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		try {
			//CanUtils.configCan0Device();
			//CanUtils.initCan0();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		initMainView();	
		// 登录设备
		//LogToService();
		// 监听指令
		//initLisenter();

	}
	@Override
	protected void onDestroy() {
		//当activity销毁 释放资源
		
		EMClient.getInstance().chatManager().removeMessageListener(msgListener);
		msgListener = null;
		super.onDestroy();
		
	}
	private void initMainView() {
		// slidingMenu的组成 ：above，behind
		// above: 内容部分
		// behind: 菜单部分

		// 给内容部分设置布局
		setContentView(R.layout.activity_main);

		// 给菜单部分设置布局:左边的滑动布局
		setBehindContentView(R.layout.home_menu_left);
		// 获得菜单
		SlidingMenu slidingMenu = getSlidingMenu();
		// 设置为左右模式
		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		// 设置菜单的宽度
		slidingMenu.setBehindWidth(300);

		// 设置SlidingMenu Above的触摸模式
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// 设置behind scrollScale
		slidingMenu.setBehindScrollScale(0.5f);
		// 设置shadow
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		// 设置fade:渐入渐出的效果
		slidingMenu.setFadeDegree(0.5f);
		slidingMenu.setSlidingEnabled(true);
		// 拆分左右
		initFragment();

	}

	private void initLisenter() {
		//子线程
		msgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                EMMessage message = messages.get(0);
                EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                final String message1 = txtBody.getMessage();
                Log.e("onMessageReceived", message1);
                Gson gson = new Gson();
                final CmdBean cmdBean = gson.fromJson(message1, CmdBean.class);
                
                UIUtils.getMainHandler().post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(MainUi.this, message1, Toast.LENGTH_LONG).show();
						if (cmdBean.CmdType == Constant.LED_TYPE) {
							switch (cmdBean.LedId) {
							case 1:
								led8_Flag = cmdBean.isOn? (byte)1 : (byte)0;
								break;
							case 2:
								led9_Flag = cmdBean.isOn? (byte)1 : (byte)0;
								break;
							case 3:
								led10_Flag = cmdBean.isOn? (byte)1 : (byte)0;
							default:
								break;
							}
					
							//CanUtils.sendCan0Data(new byte[]{ctl_Flag, led8_Flag, led9_Flag, led10_Flag, 0, 0, 0, 0});
						}
					}
				});
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                EMMessage message = messages.get(0);
                EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                String message1 = txtBody.getMessage();
                Log.e("onCmdMessageReceived", message1);

//                if (message1.equals(true)){
//                    mToggleTv.setText("开");
//                }else {
//                    mToggleTv.setText("关");
//                }


                //收到透传消息
            }
            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
                EMMessage message = messages.get(0);
                EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                String message1 = txtBody.getMessage();
                Log.e("onCmdMessageReceived", message1);
                //收到已读回执
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> messages) {
                EMMessage message = messages.get(0);
                EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                String message1 = txtBody.getMessage();
                Log.e("AckReceived",message1);
                //收到已送达回执
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        
       
	}
	
	
	private void handlerMessage(String json) {
		Toast.makeText(this, json, Toast.LENGTH_LONG).show();
		
	}
	
	
	private void initFragment() {
		//拿到碎片管理者
		FragmentManager fm = getSupportFragmentManager();
		// 开启事务
		FragmentTransaction transaction = fm.beginTransaction();
		// 加载左侧,替换掉home_menu_container,滑动窗口的
		transaction.replace(R.id.home_menu_container, new LeftFragment(),
				TAG_MENU);
		// 加载内容部分,替换掉home_content_container,中间的布局,也就是activity_main.xml的那个布局文件
		transaction.replace(R.id.home_content_container, new ContentFragment(),
				TAG_CONTENT);
		transaction.commit();
	}

	/**
	 * 获得左侧菜单
	 * 
	 * @return
	 */
	public LeftFragment getLeftFragment() {
		FragmentManager fm = getSupportFragmentManager();
		return (LeftFragment) fm.findFragmentByTag(TAG_MENU);
	}

	/**
	 * 内容区域的fragment
	 * 
	 * @return
	 */
	public ContentFragment getContentFragment() {
		FragmentManager fm = getSupportFragmentManager();
		return (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);
	}

	private void LogToService() {
		showProgressDialog(true);
		// 延时2秒
		UIUtils.SafeExecuteRunnableDelayed(new Runnable() {	
			@Override
			public void run() {
				// 登录 回调函数在子线程内
				EMClient.getInstance().login("Device1.1", "111",
					new EMCallBack() {// 回调
						@Override
						public void onSuccess() {
							Log.d("main", "登录聊天服务器成功");
							showProgressDialog(false);
							UIUtils.SafeExecuteRunnable(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(MainUi.this, "登录聊天服务器成功", Toast.LENGTH_LONG).show();
								}
							});
						}
						@Override
						public void onProgress(int progress,
								String status) {
						}

						@Override
						public void onError(int code, String message) {
							Log.d("main", "登录聊天服务器失败！");
							showProgressDialog(false);
							UIUtils.SafeExecuteRunnable(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(MainUi.this, "登录聊天服务器失败！", Toast.LENGTH_LONG).show();
								}
							});
						}
					});
			}
		}, 2000);
	}

	/**
	 * 显示取消加载框
	 * 
	 * */
	protected void showProgressDialog(boolean isShow) {
		if (mLoadingDialog == null) {
			// 取值初始化
			mLoadingDialog = new LoadingDialog(MainUi.this);
			mLoadingDialog.setMessage("正在加载...");
		}
		if (isShow) {
			if (!mLoadingDialog.isShowing())
				mLoadingDialog.show();
		} else {
			if (mLoadingDialog.isShowing())
				mLoadingDialog.dismiss();
		}
	}
}
