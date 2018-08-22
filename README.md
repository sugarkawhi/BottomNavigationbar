# BottomNavigationbar

## 使用
```java
dependencies {
  compile 'me.sugarkawhi:BottomNavigationBar:1.2.2'
}
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

```Java
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

| 属性名| 描述 |
|:--:|:--|
|bnb_layoutId|此项必须设置 自定义Item布局文件|
|bnb_selectedColor|选中字体颜色 默认#000000|
|bnb_unSelectedColor|未选中字体颜色 默认#999999|
|bnb_anim|是否开启动画 默认false|
|bnb_scale_ratio|开启动画后缩放程度 默认1.1f anim为false不生效|

###### 说明
<font color=#FF0000 >bnb_layoutId 布局文件有严格的要求，否则出错</font>
+ ImageView id 必须为 bnb_item_icon
+ 如果是图片+文字 添加TextView 并且其id 必须为 bnb_item_text
+ 如果是未读消息 添加 TextView 并且其id 必须为bnb_item_badge

## 步骤

### STEP1 创建BottomNavigationEntity的列表
 

```JAVA
 List<BottomNavigationEntity> mEntities = new ArrayList<>();

        mEntities.add(new BottomNavigationEntity(
                "图片",
                R.drawable.ic_tab_album_default,
                R.drawable.ic_tab_album_selected));
        mEntities.add(new BottomNavigationEntity(
                "视频",
                R.drawable.ic_tab_img_default,
                R.drawable.ic_tab_img_selected));
        mEntities.add(new BottomNavigationEntity(
                "关注",
                R.drawable.ic_tab_news_default,
                R.drawable.ic_tab_news_selected));
        mEntities.add(new BottomNavigationEntity(
                "我的",
                R.drawable.ic_tab_avatar_default,
                R.drawable.ic_tab_avatar_selected, 10));        
```

### STEP2 创建BottomNavigationEntity的列表

```JAVA
 bottomNavigationBar.setEntities(mEntities);

```
### STEP3 设置监听

```JAVA
    //点击item
     bottomNavigationBar.setBnbItemSelectListener(new BottomNavigationBar.IBnbItemSelectListener() {

            @Override
            public void onBnbItemSelect(int position) {

            }
        });
        //重复点击
        bottomNavigationBar.setBnbItemDoubleClickListener(new BottomNavigationBar.IBnbItemDoubleClickListener() {
            @Override
            public void onBnbItemDoubleClick(int position) {
            }
        });
        
```
* * *


