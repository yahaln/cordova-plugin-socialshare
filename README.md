# cordova-plugin-socialshare
社会化分享

# 插件编译cordova版本

v8.0.0

# 支持的平台

Android

# 安装Install
```
cordova plugin add https://github.com/yahaln/cordova-plugin-socialshare.git --variable WECHAT_APPID=your_wechat_appid --variable QQ_APPID=your_qq_appid
```
# 卸载Uninstall
```
cordova plugin rm cordova-plugin-socialshare;
```
# 使用方法

（1）分享文字
```
    	function shareText(){
    		var shareOptions={};
    		shareOptions["title"]="测试";
    		shareOptions["shareText"]="测试内容";
    		shareOptions["description"]="测试描述";
    		shareOptions["type"]="friend";
    		
    		window.cordova.plugins.SharePlugin.wechatShareText(shareOptions,function(success){
    			console.log("成功：");
    			console.log(success);
    		},function(err){
    			console.log("失败：");
    			console.log(err);
    		});
    	}

```

（2）分享图片
```
    	function shareImage(){
    		var shareOptions={};
    		shareOptions["title"]="测试";
    		shareOptions["pathOrUrl"]="http://img.zcool.cn/community/010f87596f13e6a8012193a363df45.jpg@1280w_1l_2o_100sh.jpg";
    		shareOptions["description"]="测试描述";
    		shareOptions["type"]="friend";

    		window.cordova.plugins.SharePlugin.wechatShareImage(shareOptions,function(success){
    			console.log("成功：");
    			console.log(success);
    		},function(err){
    			console.log("失败：");
    			console.log(err);
    		});
    	}
```
（3）分享网页
```
    	function shareWebPage(){
    		var shareOptions={};
    		shareOptions["title"]="测试";
    		shareOptions["webPageUrl"]="http://www.baidu.com";
    		shareOptions["imageUrl"]="http://img.zcool.cn/community/010f87596f13e6a8012193a363df45.jpg@1280w_1l_2o_100sh.jpg";
    		shareOptions["description"]="测试描述";
    		shareOptions["type"]="friend";

    		window.cordova.plugins.SharePlugin.wechatShareWebPage(shareOptions,function(success){
    			console.log("成功：");
    			console.log(success);
    		},function(err){
    			console.log("失败：");
    			console.log(err);
    		});
    	}
```
（4）QQ分享图文
```
    	function QQshareTextImage(){
    		var shareOptions={};
    		shareOptions["title"]="测试";
    		shareOptions["targetUrl"]="http://www.baidu.com";
    		shareOptions["imageUrl"]="http://img.zcool.cn/community/010f87596f13e6a8012193a363df45.jpg@1280w_1l_2o_100sh.jpg";
    		shareOptions["summary"]="测试描述";

    		window.cordova.plugins.SocialShare.QQShareImageText(shareOptions,function(success){
    			console.log("成功：");
    			console.log(success);
    		},function(err){
    			console.log("失败：");
    			console.log(err);
    		});
    	}
```
（4）QQ空间分享图文
```
    	function QQZoneShareTextImage(){
    		var shareOptions={};
    		shareOptions["title"]="测试";
    		shareOptions["targetUrl"]="http://www.baidu.com";
    		shareOptions["imageUrl"]="http://img.zcool.cn/community/010f87596f13e6a8012193a363df45.jpg@1280w_1l_2o_100sh.jpg";
    		shareOptions["summary"]="测试描述";

    		window.cordova.plugins.SocialShare.QQZoneShareImageText(shareOptions,function(success){
    			console.log("成功：");
    			console.log(success);
    		},function(err){
    			console.log("失败：");
    			console.log(err);
    		});
    	}
```
# ionic V2/v3使用方法

(1)复制cordova-plugin-share/wechatshare文件夹到node_modules/@ionic-native文件夹下

(2)app.modules.ts中添加引用
```
 import {WechatShare} from "@ionic-native/wechatshare";
	
 @NgModule({
  declarations: [
    MyApp
  ],
  imports: [
  
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp
  ],
  providers: [
    StatusBar,
    SplashScreen,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
	....
    WechatShare
  ]
})
export class AppModule {}
	
```
(3)页面中使用方法

```
import {WechatShare} from "@ionic-native/wechatshare";

@IonicPage()
@Component({
  selector: 'page-mine',
  templateUrl: 'mine.html'
})
export class MinePage {

  constructor(public navCtrl: NavController,private wechatShare:WechatShare) {
  
  }
  
  public share(){
	let shareOptions={};
	shareOptions["title"]="测试";
	shareOptions["shareText"]="测试内容";
	shareOptions["description"]="测试描述";
	shareOptions["type"]="friend";
	this.wechatShare.wechatShareText(shareOptions)
	.then(data=>{
		this.tipsService.showInfo("分享成功！");
	}).catch(err=>{
		this.tipsService.showInfo("分享失败！");
	});
  }
}
```
