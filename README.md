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

注：微信分享'type'参数值：'friend'：好友,'session':朋友圈,'favourite':收藏


（1）分享文字
```
    	function shareText(){
    		var shareOptions={};
    		shareOptions["title"]="测试";
    		shareOptions["shareText"]="测试内容";
    		shareOptions["description"]="测试描述";
    		shareOptions["type"]="friend";
    		
    		window.cordova.plugins.SocialShare.wechatShareText(shareOptions,function(success){
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

    		window.cordova.plugins.SocialShare.wechatShareImage(shareOptions,function(success){
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
    		shareOptions["webPageUrl"]="https://github.com/yahaln/cordova-plugin-socialshare.git";
    		shareOptions["imageUrl"]="http://img.zcool.cn/community/010f87596f13e6a8012193a363df45.jpg@1280w_1l_2o_100sh.jpg";
    		shareOptions["description"]="测试描述";
    		shareOptions["type"]="friend";

    		window.cordova.plugins.SocialShare.wechatShareWebPage(shareOptions,function(success){
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
    		shareOptions["targetUrl"]="https://github.com/yahaln/cordova-plugin-socialshare.git";
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
（5）QQ空间分享图文
```
    	function QQZoneShareTextImage(){
    		var shareOptions={};
    		shareOptions["title"]="测试";
    		shareOptions["targetUrl"]="https://github.com/yahaln/cordova-plugin-socialshare.git";
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

(1)安装native插件
```
npm install git+https://github.com/yahaln/socialshare-ionic-native.git

```
(2)app.modules.ts中添加引用
```
 import {SocialShare} from "@ionic-native/socialshare";
	
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
    SocialShare
  ]
})
export class AppModule {}
	
```
(3)页面中使用方法

```
import {SocialShare} from "@ionic-native/socialshare";

@IonicPage()
@Component({
  selector: 'page-mine',
  templateUrl: 'mine.html'
})
export class MinePage {

  constructor(public navCtrl: NavController,private mSocialShare:SocialShare) {
  
  }
  
  public share(){
	let shareOptions={};
	shareOptions["title"]="测试";
	shareOptions["shareText"]="测试内容";
	shareOptions["description"]="测试描述";
	shareOptions["type"]="friend";
	this.mSocialShare.wechatShareText(shareOptions)
	.then(data=>{
		this.tipsService.showInfo("分享成功！");
	}).catch(err=>{
		this.tipsService.showInfo("分享失败！");
	});
  }
}
```
