package com.monster.monstersport.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.monster.monstersport.R;

import me.sugarkawhi.mreader.bean.ChapterBean;
import me.sugarkawhi.mreader.listener.IReaderTouchListener;
import me.sugarkawhi.mreader.view.MReaderView;

/**
 * Created by ZhaoZongyao on 2018/1/12.
 */

public class ReaderActivity extends AppCompatActivity {

    MReaderView readView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_reader);
        readView = findViewById(R.id.readerView);
        ChapterBean chapter = new ChapterBean();
        chapter.setBookName("《三国演义》");
        chapter.setChapterName("第一章 刘关张桃园三结义 鲁智深拳打镇关西");
        chapter.setChapterContent(TEST_CONTENT);
        readView.setChapter(chapter);
        readView.setElectric(0.6f);
        readView.setTime("BJ TIME：19:20");
        readView.setReaderTouchListener(new IReaderTouchListener() {
            @Override
            public boolean canTouch() {
                return true;
            }

            @Override
            public void onTouchCenter() {
                Toast.makeText(ReaderActivity.this, "onTouchCenter", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onTouchRight() {
                return false;
            }

            @Override
            public boolean onTouchLeft() {
                return false;
            }
        });
        hideSystemUI();
    }


    /**
     * 隐藏菜单。沉浸式阅读
     */
    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    String TEST_CONTENT = "　　“你们别顾着拍照啊！快救救她，你们有车的快救救她啊！”\n　　十字路口边，车子堵成了长龙，在最前方，许多人拿着手机在围观拍照。" +
            "\n　　空气朦胧，天空阴森着脸，给人一种神伤。" +
            "\n　　一个上衣桃领衫，下身紧身牛仔裤的女生在撕心裂肺地冲他们喊着，可似乎没人愿意理她。\n" +
            "　　她的手上抱着一位头破血流的老人，那满头的白发被血染红，脸色苍白，毫无血色。\n" +
            "　　地上到处都是血，从老人身体流出来的血。夏小贝泪流满面，抱着老人的头，边哭边鼓励着:“Hello,man?Why?What's your name?I'm fine.3q”" +
            "\n　　老人并没有力气回应她，只是那若有若无的气息，着实让人担心。夏小贝瞪着那些围观群众，眼神中散发的尽是怒火。" +
            "\n　　她大吼着:“谁有车！”" +
            "\n　　她大吼着:“谁有车！”不知是不是，她的模样太恐怖了，那些围观群众，纷纷退后不是他们没车而是不愿意,揽上这种事吧！或许，这才是现实。夏小贝低着头，望着怀中奄奄一息的老人，紧紧地握着拳头，她恨他们的无情，也恨自己的无能！\n" +
            "　　“我有。”" +
            "\n　　如磁性般的声音，如此动听。" +
            "\n　　夏小贝移了移头，一双黑色的皮鞋，一条灰色的西裤。" +
            "\n　　她抬头望着那个人，灰色的小外套，棕色的头发，白皙的皮肤，精致的五官。" +
            "\n　　他的耳朵上戴了一颗月牙耳钉，上面似乎刻了字，很是特别。\n" +
            "　　眼睛里犹如一潭深水，清澈却不见底，眼角边还泛着泪光。\n" +
            "　　高而挺拔的鼻梁下，那张看起来很有弹性的嘴唇。" +
            "\n　　如此美男，换做平时，夏小贝绝对会犯花痴。\n" +
            "　　可现在不是时候！\n" +
            "　　“可以带我们去最近的大医院吗？”夏小贝满怀期待地望着那人，眼神中流露的是可怜兮兮。\n" +
            "　　可那人没有看她，而是盯着她怀中的老人，脸色苍白，奄奄一息。\n　　而围观了那么多人，却始终没有人愿意搭把手。\n" +
            "　　“好，我的车就在旁边，你去开车门，我来抱……”那人走过来，伸出手准备弓腰。\n　　夏小贝一把抱起老人，连口气都不喘，大方地说:“车在哪里？”\n" +
            "　　如此瘦小，却蹲着将老人一把抱起，还面不红，心不跳，真让人惊讶！\\n　　那人就被她的举动惊到了，站在原地发呆。\\n　　“还愣着干嘛？”夏小贝焦急地吼了一声，那人才回过神来，将她带到了车前。\\n　　那人开着车，一直望着镜中的夏小贝。\\n　　夏小贝抱着老人，眼泪总是不听使唤地掉了下来，她不停地安慰着老人，鼓励着老人:“您一定要坚持住，我们马上就要到医院了。再坚持一会儿，就一会儿……”\\n　　她哽咽着，想起了刚见到那人时的场景。\\n　　美男盯着老人，眼角闪着泪光，眼神中也是满满的忧伤。\\n　　“那个，你为什么要帮忙？”夏小贝还是忍不住心里的好奇，问了出来。\\n　　美男半天没有吭声，专注地开着车，加快了速度，却又很平稳。\\n　　许久，才回了话:“如果我也不救她，那些人绝对能看着她死。”\\n　　虽然简简单单的理由，可他的神情变了，尤其说到后面，手还紧紧地抓着方向盘。\\n　　终于，车内安静了，没几分钟便到了医院。\\n　　夏小贝轻轻地抱起老人，匆忙地下了车，不小心撞到了头。\\n　　“啊！”夏小贝发出了呻吟，但也没多停留，抱着老人迅速地跑进了医院。\\n　　车内的人望着她的身影，不禁嘴角微微上扬。\\n　　“医生！医生！”\\n　　随着夏小贝的呼喊，几个护士推来了轮床，将老人小心翼翼地放在了床上，推进了急救室。\\n　　夏小贝站在急救室外，两只手合拢，闭上了双眼，嘴里念叨着:“菩萨保佑，菩萨保佑，一定要保佑这位奶奶能够活下来！阿弥陀佛……”\\n　　“谁是家属？家属去交下费用，老人家需要动手术，需要家属签字。”一位护士跑过来，语气有些急促。\\n　　费用……动手术……\\n　　完了！这下真完了！\\n　　夏小贝咬了咬嘴唇，拿出自己的小钱包数了数。\\n　　天呐，只剩下五百多块钱了！这点钱哪够老人家动手术的呀？怎么办？\\n　　“老人家手术片刻不能耽搁的，家属可想好了？”护士在一旁催着，直愣愣地盯着夏小贝。\\n　　“我……我……”夏小贝将话卡在了喉咙里，眼睛不停地眨着，用力地抠着手指头。\\n　　“刷我的，尽快手术。”\\n　　简洁的话语，熟悉的磁性般的声音。夏小贝望着他，还是那双清澈却深邃的眼睛。\\n　　“我还以为你已经走了。”夏小贝抓了抓后脑勺，难为情地笑着，大大的眼睛眯成了一条线，可爱极了。\\n　　“这钱你是要还的，我可不是什么慈善家。”那人开了口，瞬间感觉好感降了一些。\\n　　“要还也不是我还呀！更何况，我哪里有钱还你啊！”夏小贝撑开钱包，放到那人眼前，说:“看吧，这是我全身家当了。”\\n　　“没钱？那就以身相许。”那人说完便弯过身子靠了过来。\\n　　那俊美的脸瞬间放大了数倍，脸上带着略微的坏笑。\\n　　吓得夏小贝连忙退后，低着头，只感觉脸颊好烫，心脏也跳的很快。\\n　　美男依旧不依不饶，步步紧逼，直到墙角，无路可退。\\n　　他伸出手臂，脸慢慢地靠近，都能感觉到他的呼吸的气息了。\\n　　靠，这是被壁咚了啊！\\n　　夏小贝用力地推开他，却没想到用力过猛，他狠狠地撞到了墙上。\\n　　“天呐，对，对不起啊！我，我没控制好力气……你，你没事吧？”夏小贝走了过去，抓着他的手臂转了个圈。\\n　　突然，美男一把搂住她的腰，两个人的身体紧紧地贴到了一块。\\n　　夏小贝脸颊通红，明显感觉气息也更加急促了。\\n　　他的脸越靠越近，那性感的嘴唇也渐渐地接近……\\n　　咕咚～\\n　　夏小贝咽了咽口水，眼睛不停地眨着，靠在那美男的怀里一动不动。\\n　　“病人家属，麻烦签下字。”护士来的就这么巧，马上就……\\n　　哎呀！丢死人了！\\n　　夏小贝连忙挣脱他的怀抱，低着头，假意咳嗽了两声。\\n　　护士走了过来，拿了份手术协议。\\n　　“那个，护士，我不是病人家属……”\\n　　夏小贝吞吞吐吐地说着，救人她能做，可是这必须要家属签的，她不过是个路过的。\\n　　“您不是病人家属？”护士有些惊讶，反问了她一遍。\\n　　她微微点了点头，“嗯”了一声。\\n　　这就尴尬了。\\n　　护士一时之间也说不出什么来，只是望着她身后的美男。\\n　　“您……”\\n　　还未等护士开口，美男便说了出来:“我不过是顺路，送她们过来。”\\n　　……\\n　　这下护士着急了，没有家属签字，手术便不能继续啊！\\n　　护士赶紧走了，应该是去找老人家属了吧！\\n　　夏小贝望着护士离开，不经意间叹了口气:“唉。”\\n　　希望能快点找到老人家属吧！不然，就白忙活了一场了。\\n　　“喂！”\\n　　“嗯？”夏小贝自然反应地应了一声，转过身来。\\n　　却不知那美男什么时候站在她的身后了，这一转身，刚好撞进了他的怀里。\\n　　夏小贝赶紧退后，低着头，撩了撩头发。\\n　　“你大路上救下这老人，就不怕以后有人碰瓷吗？”美男盯着她，越发走得近了。\\n　　夏小贝慢慢地退后，傻笑了几声:“嘿嘿，那个，我也没有想那么多，就是觉得，救人也只是顺手……”\\n　　那美男似乎没有在听她讲话，只是一步一步地靠近，脸上还带着略微的坏笑。\\n　　这人到底想怎么样啊？\\n　　夏小贝一步一步退后，两手抓着裤边，一会儿抬头望着他，一会儿又躲避视线。\\n　　退后，一步两步三步……\\n　　“啊！”突然，夏小贝重心失稳，后脚不知被什么东西绊倒，整个人都栽了进去。\\n　　只觉得，好像，不太好起来……\\n　　夏小贝嗅了嗅，一股臭味扑鼻而来，在这个容器里环绕着。\\n　　天，等等，这是什么？垃圾桶吗？\\n　　“垃圾桶……”夏小贝皱着眉头，使尽浑身解数，终究，还是没出来。\\n　　垃圾桶在走廊里翻滚着，垃圾也洒了一地。行走的人都停了下来，围观着，看着这出好戏。\\n　　当然，免不得有人拍照录视频的，也免不得围观群众被这翻滚的垃圾桶逗笑。\\n　　听着那些人的取笑，这也太难堪了吧！\\n　　夏小贝眼泪都在眼眶里打转了，那些人却还在那里笑，拍照，这就是现实吗？\\n　　你妹的！\\n　　夏小贝怒火中烧，两手使劲一推，终于出来了！\\n　　嘭！\\n　　一声巨响，吓到了那些围观的人。垃圾桶被狠狠地砸到了地上，破了几个洞，垃圾撒的到处都是。\\n　　“拍够了吗！”夏小贝狠狠地瞪着他们，这些现实的人。\\n　　美男走了过来，伸手将她头上的垃圾拿了下来，皱着眉头将手在她衣服上蹭了几下，满脸的嫌弃。\\n　　“啧啧，我本来还想提醒你的，谁知道你那么喜欢退后呢？”虽然他在掩饰，但脸上的笑意却是怎么都掩藏不了的。\\n　　混蛋！\\n　　夏小贝冷冷地盯着他，问:“你知道我身后有垃圾桶？”\\n　　那美男耸了耸肩，好像一副很有成就感的模样。\\n　　靠！\\n　　夏小贝握紧了拳头，一拳挥向了美男的脸。\\n　　拳头来的那么突然，真是防不胜防啊！刚好，命中了他的脸。\\n　　“你……”美男本想说些什么，可是他没有。\\n　　夏小贝瞪着他，紧紧地握着双拳，周边的人对她指手画脚，议论纷纷。\\n　　为什么会这样？不是说好人会有好报的吗？为什么会这样？\\n　　夏小贝内心有些崩溃，这很委屈，非常委屈。\\n　　不由得，眼泪流了下来。\\n　　这是个不好的相见，如果可以，真希望以后别再见到这个人！\\n　　夏小贝眼眶中的眼泪不禁自流，她望了望周围的人，每个人都在拿她当笑话看。\\n　　她抽泣着，迅速地转身离开了。\\n　　望着夏小贝离去的背影，美男心里也是不好受……\\n";
}
