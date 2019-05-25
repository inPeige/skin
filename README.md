# skin
市面上对数的App都提供换肤功能，这里暂且不讲白天和夜间模式

下图是网易云音乐的换肤功能

![经典](https://upload-images.jianshu.io/upload_images/11195468-7e7c93b52d7af746.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/118/format/webp)

![黑](https://upload-images.jianshu.io/upload_images/11195468-c021f0c829864ba7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/118/format/webp)

换肤其实就是替换资源（文字、颜色、图片等）
### 一、换肤模式：
```
1.内置换肤
 在Apk包中存在多种资源(图片、颜色值)用于换肤时候切换。
 自由度低，apk文件大  一般用于没有其他需求的日间/夜间模式app 
2.动态换肤
 通过运行时动态加载皮肤包
```
### 二、如何使用
```
//init
public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.init(this);
    }
}
```
```
//loadSkin
public void apply(View view) {
  SkinManager.getInstance().loadSkin("/sdcard/skin.apk");
}
//restore
public void restore(View view) {
   SkinManager.getInstance().loadSkin("");
}


```
