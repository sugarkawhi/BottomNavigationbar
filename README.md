# BottomNavigationbar
底部导航栏

## 使用
```
compile 'me.sugarkawhi:BottomNavigationBar:1.2.1'
```

## 功能

+ 支持 图片+文字 组合
+ 支持 仅图片 
+ 支持 未读消息
+ 支持 自定义布局
+ 支持 切换动画(可关闭)
+ 支持 选中tab再次点击事件(多用于返回顶部或刷新)

## 预览

##### 图片+文字
![图片+文字](http://olpu32iyy.bkt.clouddn.com/18-8-17/22329806.jpg)
#### 仅图片
![仅图片](http://olpu32iyy.bkt.clouddn.com/18-8-17/85464559.jpg)
#### 支持消息
![支持消息](http://olpu32iyy.bkt.clouddn.com/18-8-17/62909687.jpg)
#### 支持切换动画
![切换动画](http://olpu32iyy.bkt.clouddn.com/18-8-17/67770602.jpg)

## 使用

```
  <me.sugarkawhi.bottomnavigationbar.BottomNavigationBar
        android:id="@+id/bottomNavigationBar"
        android:layout_width="match_parent"
        android:layout_height="48dp"
        android:layout_gravity="bottom"
        app:bnb_layoutId="@layout/bnb_item_view"
        app:bnb_selectedColor="#000000"
        app:bnb_unSelectedColor="#999999"
        app:bnb_anim="false"
        app:bnb_scale_ratio="1.1" />
```
## 属性说明

app:bnb_layoutId="@layout/bnb_item_view"
        app:bnb_selectedColor="#000000"
        app:bnb_unSelectedColor="#999999"
        app:bnb_anim="false"
        app:bnb_scale_ratio="1.1f" 

| 属性名| 描述 |
|:--:|:--|
|bnb_layoutId|此项必须设置 自定义布局|
|bnb_selectedColor|选中字体颜色 默认#000000|
|bnb_unSelectedColor|未选中字体颜色 默认#999999|
|bnb_anim|是否开启动画 默认false|
|bnb_scale_ratio|开启动画后缩放程度 默认1.1f anim为false不生效|







