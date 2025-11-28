-- V1.1.1__
-- 文章模块

-- 初始化类目
INSERT into `category` (`id`, `category_name`, `status`, `deleted`)
values ('1', '后端', '1', '0'),
       ('2', '前端', '1', '0'),
       ('3', '大数据', '1', '0'),
       ('4', 'Android', '1', '0'),
       ('5', 'IOS', '1', '0'),
       ('6', '人工智能', '1', '0'),
       ('7', '开发工具', '1', '0'),
       ('8', '程序故事', '1', '0'),
       ('9', '阅读', '1', '0'),
       ('10', '编程语言', '1', '0'),
       ('11', '数据结构与算法', '1', '0'),
       ('12', '领域知识', '1', '0'),
       ('13', '面试八股', '1', '0'),
       ('14', '数据库', '1', '0'),
       ('15', '运维', '1', '0');


-- 初始化标签
INSERT into `tag` (`id`, `tag_name`, `tag_type`, `category_id`, `status`, `deleted`)
values ('1', 'Java', '1', '10', '1', '0'),
       ('2', 'Go', '1', '10', '1', '0'),
       ('3', '算法', '1', '11', '1', '0'),
       ('4', 'Python', '1', '10', '1', '0'),
       ('5', 'Spring Boot', '1', '1', '1', '0'),
       ('6', '面试', '1', '1', '13', '0'),
       ('7', 'MySQL', '1', '1', '14', '0'),
       ('8', '数据库', '1', '1', '14', '0'),
       ('9', 'Spring', '1', '1', '1', '0'),
       ('10', '架构', '1', '1', '1', '0'),
       ('11', 'C/C++', '1', '10', '1', '0'),
       ('12', 'Redis', '1', '14', '1', '0'),
       ('13', 'Linux', '1', '15', '1', '0'),
       ('14', 'JavaScript', '1', '10', '1', '0'),
       ('15', 'Vue.js', '1', '2', '1', '0'),
       ('16', 'React.js', '1', '2', '1', '0'),
       ('17', 'CSS', '1', '2', '1', '0'),
       ('18', 'TypeScript', '1', '10', '1', '0'),
       ('19', '后端', '1', '1', '1', '0'),
       ('20', 'Node.js', '1', '10', '1', '0'),
       ('21', '前端框架', '1', '2', '1', '0'),
       ('22', 'Webpack', '1', '2', '1', '0'),
       ('23', '架构', '1', '2', '1', '0'),
       ('24', '微信小程序', '1', '2', '1', '0'),
       ('25', 'GitHub', '1', '7', '1', '0'),
       ('26', 'Kotlin', '1', '10', '1', '0'),
       ('27', 'Flutter', '1', '5', '1', '0'),
       ('28', 'Android Jetpack', '1', '4', '1', '0'),
       ('29', 'APP', '1', '4', '1', '0'),
       ('30', 'Android Studio', '1', '4', '1', '0'),
       ('31', '源码', '1', '4', '1', '0'),
       ('32', '性能优化', '1', '4', '1', '0'),
       ('33', '面试', '1', '4', '1', '0'),
       ('34', '架构', '1', '4', '1', '0'),
       ('35', 'gradle', '1', '4', '1', '0'),
       ('36', '程序员', '1', '4', '1', '0'),
       ('37', 'Swift', '1', '5', '1', '0'),
       ('38', 'SwiftUI', '1', '5', '1', '0'),
       ('39', 'Flutter', '1', '5', '1', '0'),
       ('40', '题解', '1', '5', '1', '0'),
       ('41', 'Objective-C', '1', '5', '1', '0'),
       ('42', 'Mac', '1', '5', '1', '0'),
       ('43', '计算机视觉', '1', '5', '1', '0'),
       ('44', 'Apple', '1', '5', '1', '0'),
       ('45', '音视频开发', '1', '5', '1', '0'),
       ('46', '深度学习', '1', '6', '1', '0'),
       ('47', '机器学习', '1', '6', '1', '0'),
       ('48', 'PyTorch', '1', '6', '1', '0'),
       ('49', 'NLP', '1', '6', '1', '0'),
       ('50', '数据分析', '1', '6', '1', '0'),
       ('51', '神经网络', '1', '6', '1', '0'),
       ('52', 'TensorFlow', '1', '6', '1', '0'),
       ('53', '数据可视化', '1', '6', '1', '0'),
       ('54', '数据挖掘', '1', '6', '1', '0'),
       ('55', '开源', '1', '7', '1', '0'),
       ('56', 'Git', '1', '7', '1', '0'),
       ('57', 'Linux', '1', '7', '1', '0'),
       ('58', '测试', '1', '7', '1', '0'),
       ('59', '数据库', '1', '7', '1', '0'),
       ('60', 'JavaScript', '1', '7', '1', '0'),
       ('61', 'Unity3D', '1', '7', '1', '0'),
       ('62', 'Rust', '1', '7', '1', '0'),
       ('63', '大数据', '1', '7', '1', '0'),
       ('64', '架构', '1', '8', '1', '0'),
       ('65', '开源', '1', '8', '1', '0'),
       ('66', '面试', '1', '8', '1', '0'),
       ('67', '数据结构', '1', '8', '1', '0'),
       ('68', '云原生', '1', '9', '1', '0'),
       ('69', '笔记', '1', '9', '1', '0'),
       ('70', 'Serverless', '1', '9', '1', '0'),
       ('71', '容器', '1', '9', '1', '0'),
       ('72', '微服务', '1', '9', '1', '0'),
       ('73', '产品经理', '1', '9', '1', '0'),
       ('74', 'RocketMQ', '1', '9', '1', '0'),
       ('75', 'sqlite', '1', '3', '1', '0'),
       ('76', 'sql', '1', '3', '1', '0'),
       ('77', 'spark', '1', '3', '1', '0'),
       ('78', 'hive', '1', '3', '1', '0'),
       ('79', 'hbase', '1', '3', '1', '0'),
       ('80', 'hdfs', '1', '3', '1', '0'),
       ('81', 'hadoop', '1', '3', '1', '0'),
       ('82', 'rabbitmq', '1', '3', '1', '0'),
       ('83', 'postgresql', '1', '3', '1', '0'),
       ('84', '数据仓库', '1', '3', '1', '0'),
       ('85', 'oracle', '1', '3', '1', '0'),
       ('86', 'flink', '1', '3', '1', '0'),
       ('87', 'nosql', '1', '3', '1', '0'),
       ('88', 'eureka', '1', '3', '1', '0'),
       ('89', 'mongodb', '1', '3', '1', '0'),
       ('90', 'zookeeper', '1', '3', '1', '0'),
       ('91', 'elasticsearch', '1', '3', '1', '0'),
       ('92', 'kafka', '1', '3', '1', '0'),
       ('93', 'json', '1', '3', '1', '0'),
       ('94', '计算机网络', '1', '12', '1', '0'),
       ('95', 'JVM', '1', '13', '1', '0'),
       ('96', 'Java基础', '1', '13', '1', '0'),
       ('97', '操作系统', '1', '12', '1', '0');

-- 初始化文章
INSERT INTO article
(id, user_id, article_type, title, short_title, picture, summary, category_id, source, source_url, status, deleted, create_time, update_time)
VALUES(1, 1, 1, '连连看(一)', 'C++开发小游戏连连看', '', '使用C++开发小游戏连连看，使用EasyX进行图像操作', 10, 2, '', 1, 0, '2022-09-05 19:12:32', '2022-09-05 19:12:32');
INSERT INTO article
(id, user_id, article_type, title, short_title, picture, summary, category_id, source, source_url, status, deleted, create_time, update_time)
VALUES(2, 1, 1, '连连看(二)', 'C++开发小游戏连连看补充', '', 'C++开发小游戏连连看补充,完善了功能，修复了前面开发(一)的一些问题', 10, 2, '', 1, 0, '2022-09-09 19:12:32', '2022-09-09 19:12:32');
INSERT INTO article
(id, user_id, article_type, title, short_title, picture, summary, category_id, source, source_url, status, deleted, create_time, update_time, offical_stat)
VALUES(3, 1, 1, '坦克大战(一)', '', '', '开发小游戏坦克大战补充', 1, 2, '', 1, 0, '2022-09-23 20:15:27', '2022-09-23 20:15:27', 1);
INSERT INTO article
(id, user_id, article_type, title, short_title, picture, summary, category_id, source, source_url, status, deleted, create_time, update_time, offical_stat)
VALUES(4, 1, 1, '坦克大战(二)', 'C++开发小游戏坦克大战补充', '', '修复了坦克大战一的一些问题,增加了一些功能', 10, 2, '', 1, 0, '2022-10-03 12:15:20', '2022-10-03 12:15:20', 1);
INSERT INTO article
(id, user_id, article_type, title, short_title, picture, summary, category_id, source, source_url, status, deleted, create_time, update_time, offical_stat)
VALUES(5, 3, 1, '横滚角偏航角航向角理解', '', '', '最近研究IMU陀螺仪，经过一早上的调试，最后发现根本看不懂xyz三个角度的度数是怎么来的，看了一些教程，也觉得不够直观，去了b站找了个视频，发现讲解的还不错，顺便也加上自己的一些理解，但是写的可能并不是很直观，但是应该可以提供一点思路。'
                , 12, 2, '', 1, 0, '2023-04-01 20:40:12', '2023-04-01 20:40:12', 1);
INSERT INTO article
(id, user_id, article_type, title, short_title, picture, summary, category_id, source, source_url, status, deleted, create_time, update_time, offical_stat)
VALUES(6, 3, 1, '【不使用深度学习框架】多层感知机实现手写MNIST数据集识别', '经典算法手写实现', '', '手写MNIST识别是一个非常经典的问题，其数据集共有70000张28*28像素的图片，其中60000张作为训练集，剩下的10000张作为测试集，每一张图片都表示了一个手写数字，经过了灰度处理。本文延续前面文章提到的多层感知机，在这里完成更复杂的任务，这个也可以作为分类任务的代表。'
                ,6, 2, '', 1, 0, '2024-05-05 09:12:32', '2024-05-05 09:12:32', 1);
INSERT INTO article
(id, user_id, article_type, title, short_title, picture, summary, category_id, source, source_url, status, deleted, create_time, update_time, offical_stat)
VALUES(7, 4, 1, 'Tarjan算法的应用---缩点与割点', '', '', 'Tarjan主要基于深搜，其中有两个非常关键的标记数组，分别是dfn和low，同时引入概念时间戳tt，也就是到达这个点的时间，实际上就是搜索到的次序，dfn记录每个点的时间戳，即第一次访问到的号次，low也就是能到达的最早的时间戳，下面分析。'
                , 11, 2, '', 1, 0, '2024-07-10 22:45:17', '2024-07-10 22:45:17', 1);
INSERT INTO article
(id, user_id, article_type, title, short_title, picture, summary, category_id, source, source_url, status, deleted, create_time, update_time, offical_stat)
VALUES(8, 4, 1, 'This Message Will Self-Destruct in 5s Editorial', '一道题解', '', '引入map存储满足和i匹配的j+h[j]的j的数量，可以看到对于任意一组i，j，在扫描到i时， j 是匹配的，在扫描到 j 时，i 一定也是满足式子的，所以可以把计算分为两个阶段，扫描 i 时标记这一对，扫描到 j 时将这一对收集到答案中，体现到代码中其实...'
                , 11, 2, '', 1, 0, '2022-07-13 15:10:42', '2022-07-13 15:10:42', 1);
INSERT INTO article
(id, user_id, article_type, title, short_title, picture, summary, category_id, source, source_url, status, deleted, create_time, update_time, offical_stat)
VALUES(9, 5, 1, 'Mysql存储过程和游标的一点理解', 'Mysql学习记录', '', '存储过程的关键字是precedure，我们create一个新的precedure，然后前面说了存储过程就是函数，你得给他要算的人，所以把参数列表里面的empno前面加上一个in，表示是输入，因为你还要把值输出的，sql里面有没有return我不知道，但是我可以把参数列表里面的变量标记为out，表示输出，类似于其他语言的引用传参，如下;数据都是乱凑的，请勿当真。在我理解来其实就是编程语言里面常说的函数，了解过其他语言的同学对于函数不会陌生，不过这个名字是真的高大上，本质就是把值传进去，然后计算好把值传出来。'
                , 7, 2, '', 1, 0, '2022-12-02 22:45:17', '2022-12-02 22:45:17', 1);
INSERT INTO article
(id, user_id, article_type, title, short_title, picture, summary, category_id, source, source_url, status, deleted, create_time, update_time, offical_stat)
VALUES(10, 4, 1, '图论----最短路的应用', '图论学习记录', '', '这一篇主要记录几个关于最短路的题目，主要是基于Dijkstra和Floyd算法，由于Dijkstra不考虑负边权，所以这一篇没有考虑负边的问题，主要是几个实际应用？'
                , 11, 2, '', 1, 0, '2024-07-10 22:45:17', '2024-07-10 22:45:17', 1);
INSERT INTO article
(id, user_id, article_type, title, short_title, picture, summary, category_id, source, source_url, status, deleted, create_time, update_time, offical_stat)
VALUES(11, 5, 1, '图论---拓扑排序的应用', '图论学习记录', '', '最近研究了几道图论的题目，都是图论入门的算法，用的比较多的就是拓扑排序，多用于有着先后顺序的题目，也可以用来判断环，做个小总结。'
                , 11, 2, '', 1, 0, '2023-01-01 21:20:30', '2023-01-01 21:20:30', 1);



-- 初始化文章标签表
-- 连连看(一)  C++ / EasyX 小游戏
INSERT INTO article_tag(article_id,tag_id,deleted,create_time,update_time)
VALUES (1,11,0,'2025-10-20 21:20:11','2025-10-20 21:20:11');
-- 连连看(二)  同上
INSERT INTO article_tag(article_id,tag_id,deleted,create_time,update_time)
VALUES (2,11,0,'2025-10-20 21:20:11','2025-10-20 21:20:11');
-- 坦克大战(一)  C++ / EasyX 小游戏
INSERT INTO article_tag(article_id,tag_id,deleted,create_time,update_time)
VALUES (3,11,0,'2025-10-20 21:20:11','2025-10-20 21:20:11');
-- 坦克大战(二)  同上
INSERT INTO article_tag(article_id,tag_id,deleted,create_time,update_time)
VALUES (4,11,0,'2025-10-20 21:20:11','2025-10-20 21:20:11');
-- 横滚角偏航角  IMU
INSERT INTO article_tag(article_id,tag_id,deleted,create_time,update_time)
VALUES (5,61,0,'2025-10-20 21:20:11','2025-10-20 21:20:11');
INSERT INTO article_tag(article_id,tag_id,deleted,create_time,update_time)
VALUES (5,69,0,'2025-10-20 21:20:11','2025-10-20 21:20:11');
-- 手写 MNIST  Python + 多层感知机
INSERT INTO article_tag(article_id,tag_id,deleted,create_time,update_time)
VALUES (6,51,0,'2025-10-20 21:20:11','2025-10-20 21:20:11');
INSERT INTO article_tag(article_id,tag_id,deleted,create_time,update_time)
VALUES (6,46,0,'2025-10-20 21:20:11','2025-10-20 21:20:11');
-- Tarjan 缩点割点  C++ 图论
INSERT INTO article_tag(article_id,tag_id,deleted,create_time,update_time)
VALUES (7,3,0,'2025-10-20 21:20:11','2025-10-20 21:20:11');
INSERT INTO article_tag(article_id,tag_id,deleted,create_time,update_time)
VALUES (7,40,0,'2025-10-20 21:20:11','2025-10-20 21:20:11');
INSERT INTO article_tag(article_id,tag_id,deleted,create_time,update_time)
VALUES (7,67,0,'2025-10-20 21:20:11','2025-10-20 21:20:11');
-- Editorial 题解  C++ 刷题
INSERT INTO article_tag(article_id,tag_id,deleted,create_time,update_time)
VALUES (8,3,0,'2025-10-20 21:20:11','2025-10-20 21:20:11');
INSERT INTO article_tag(article_id,tag_id,deleted,create_time,update_time)
VALUES (8,40,0,'2025-10-20 21:20:11','2025-10-20 21:20:11');
INSERT INTO article_tag(article_id,tag_id,deleted,create_time,update_time)
VALUES (8,67,0,'2025-10-20 21:20:11','2025-10-20 21:20:11');
-- MySQL 存储过程  数据库
INSERT INTO article_tag(article_id,tag_id,deleted,create_time,update_time)
VALUES (9,7,0,'2025-10-20 21:20:11','2025-10-20 21:20:11');
-- 图论最短路  C++ 算法
INSERT INTO article_tag(article_id,tag_id,deleted,create_time,update_time)
VALUES (10,3,0,'2025-10-20 21:20:11','2025-10-20 21:20:11');
INSERT INTO article_tag(article_id,tag_id,deleted,create_time,update_time)
VALUES (10,67,0,'2025-10-20 21:20:11','2025-10-20 21:20:11');
INSERT INTO article_tag(article_id,tag_id,deleted,create_time,update_time)
VALUES (10,40,0,'2025-10-20 21:20:11','2025-10-20 21:20:11');
-- 拓扑排序  C++ 图论
INSERT INTO article_tag(article_id,tag_id,deleted,create_time,update_time)
VALUES (11,3,0,'2025-10-20 21:20:11','2025-10-20 21:20:11');
INSERT INTO article_tag(article_id,tag_id,deleted,create_time,update_time)
VALUES (11,40,0,'2025-10-20 21:20:11','2025-10-20 21:20:11');
INSERT INTO article_tag(article_id,tag_id,deleted,create_time,update_time)
VALUES (11,67,0,'2025-10-20 21:20:11','2025-10-20 21:20:11');


-- 初始化文章详情
INSERT INTO article_detail(article_id,version,content,deleted,create_time,update_time) VALUES(1,1,_utf8mb4'> 几个月之前就想要开发一个连连看小游戏，但是当时在算法这一步上面卡住了，之后也就不了了之。暑假末期将至，突然又想起了这个想法，正好这时候也有了一点思路，打了十几个小时终于最后做出了一个差不多的模样，写一篇笔记记录一下。
>
> 至于标题为什么写伪C++，主要是因为在开发过程中基本全是利用了C的思想，全程面向过程去开发，用到C++仅仅是几个STL函数。
>
> 最终的效果大致如下:

# ![img](https://s21.ax1x.com/2025/11/24/pZAk674.png)

 前期准备

> 这里主要是关于两个相关的前期准备，一个是图形可视化库EasyX库的安装，然后是准备图案。

## EasyX

首先开发一个小游戏需要一些图形化的界面，这里我用了easyx，需要一些文件的操作，可以搜索一下怎么下载，网上有很多资源 。

## 图案

对于消除图案的选择，这个可以随便定，动物植物都可以，在这里我选了水果，非常呆萌，图案的下载可以去[iconfont-阿里巴巴矢量图标库](https://www.iconfont.cn/)，有许多矢量图资源。

对于消除线我整理之后大概有六种基本的线型，分别是竖线，横线，左上，左下，右上，右下。这个我是用的ppt制作之后直接截图，然后把背景扣掉了，截图的大小最好与上文的消除图案大小一致，为了后面统一修改大小，尽量对齐。我就没对齐，所以看起来还是有点别扭

![img](https://s21.ax1x.com/2025/11/24/pZAkyBF.png)

修改好大小之后放到这个路径下，与对应项目的debug同级的。

![img](https://s21.ax1x.com/2025/11/24/pZAksnU.png)

# 预热程序---番外篇

> 为什么叫番外篇，因为这一段其实是我研究一些算法用到的，并不是真正项目，但是后面的开发也基于这几个重要的程序，就是添枝加叶的问题，所以单独拉出来说一说。

## 判定消除

### 引入

连连看消除的规则非常简单，只要待消除的**两个图案相同并且可以通过一个转弯不超过两次的折线连接**便可以成功消除，基于这个原则，考虑将图案先简化为一个个点，形成一个点阵，这时候待消除的两个点可以视为是起点和终点，然后就是搜索路径。

搜索路径的方法有多种，主要分为深度优先搜索和广度优先搜索。因为转弯最多就两次，所以很多时候连线都是一条比较直的线，这很像是一直向前走的深度优先搜索，所以我就考虑用深度优先搜索。所以调了不知道多久



### 易混点

为了后面的程序统一性，这里取到的X轴Y轴是和EasyX的定义一致的，也就是X轴向右，Y轴向下，所以这时候的Y值其实是行值，X值其实是列值，对于矩阵 a [ ] [ ] ,xy分别是XY坐标，那么对应的点其实是a[y][x]。

还有一点因为连连看的连线是可以在游戏矩阵之外的，所以我在外面也扩充了一圈，这样的话在外面的就相当于也是迷宫中的路，同时因为扩充了之后第0行和0列都被占用了，所以这时候的真正游戏界面就是从1下标开始。

### DFS寻找路径

起点设置为S，终点设置为T，我们需要的是还原路径，所以在过程中不仅需要记录坐标，还要记录是横线还是竖线（对应的就是上下走还是左右走），我是将其放在在栈中。

dfs递归搜索的过程中需要的参数分别是当前坐标，起点终点坐标，当前转弯的个数，方向参数，查找情况。

几个坐标好理解，需要注意的就是坐标都是对应的**x值为列值**；转弯次数就是从起点走到这个点走了多少步，为了记录，我单独开了一个flag数组来记录步数（一开始本来是用来标记访问的，后来发现没有必要），**flag[i][j]表明的意思就是从起点(qdx,qdy)走到当前位置(j,i)的最小步数；**方向参数好理解，就是方向，**每个方向对应的是方向数组的索引**，方向数组分为两个，一个对应x轴，一个对应y轴，也就是程序中的dirx和diry，一个索引对应两个dir值，分别加上当前的xy坐标值可以实现上下左右的移动，设置这个参数主要是为了可以让下一次的查找还是从这个方向开始，就可以实现一个类似于可以向前一直向前走的这样一个操作，当然需要注意的是不能反方向走，在遇到无效方向是要及时continue；还有一个参数是引用参数，是判断当前是否已经找到答案的，要是找到了，直接退出。

在后面程序的测试中发现可能还是存在一些bug，但是目前还不知道哪里存在问题，但是判定消除基本无误。

```cpp
void dfs(int x,int y, int qdx, int qdy,int zdx,int zdy,int step,int now,int &stats) {
	if (stats)return;//找到了就不找了
	//判断,参数分别是x,y,(x向右,y向下),起点xy和终点xy,当前转弯次数,当前方向参数，0123分别是上右下左,当前寻找的状态
	if (step == 3) {//超出步数或者障碍物
		if (!st.empty()) {//拿出去
			st.pop();
		}
		return;
	}
	if (zdx==x&&zdy==y&&step <= 2) {//终点
		stats = 1;
		st.pop();//终点不需要变成横线
		return;
	}
	for (int i = now; i < now + 4; i++) {//从当前方向开始找
		int index = i % 4;
		int xx = x + dirx[index];
		int yy = y + diry[index];
		if (i == now + 2&&x!=qdx&&y!=qdy || xx < 0 || yy < 0 || xx == N || yy == N||
			mat[yy][xx]&&!(xx==zdx&&yy==zdy))//不合法,最后一个代表已经被占据并且不是终点
			continue;
		if (now == index|| x == qdx && y == qdy) {//方向相同或者是起点
			flag[yy][xx] = min(flag[y][x], flag[yy][xx]);//不需要加转弯次数
			st.push({ xx,yy ,i%2});//当前区块信息入栈
			dfs(xx, yy, qdx,qdy,zdx,zdy,step, index, stats);
		}
		else {
			flag[yy][xx] = min(flag[y][x] + 1, flag[yy][xx]);//需要加转弯次数
			st.push({ xx,yy,now%2 });
			dfs(xx, yy, qdx, qdy, zdx, zdy, step + 1, index, stats);
		}
		if (stats)return;
	}
	if (stats)return;
	if (!st.empty()) {//四个方向都没有
		st.pop();
	}
	return ;
}
```



## 矩阵生成

下面就是矩阵的生成，首先先把扩充后的点阵所有值初始化为0，0在后面就是代表空，也就是不显示。

因为要保证程序尽量可以消除，所以为了避免很多不能消除，我在生成时基于以下原则：先确定点种类数（1--cag），然后开始生成，随机找两个点，判定能否消除，如果可以，那么确定下来两个点对应都是当前这个种类的点，如果不能消除，那么计次，如果达到一百次，也就是找了一百组点都没找到可以消除的，那么认为当前已经到了生成的最大值，完成矩阵生成。这样做的好处就是**每一组点都是基于可以消除生成**的，所以解很比较多，而不至于很快就没有解。

```cpp
int create_mat() {
	srand((unsigned)time(NULL));
	//生成N-2*N-2大小游戏矩阵,目前采用的算法是随机生成，可能最后不能填满...
	int max_num = (N - 2)*(N - 2);
	bool t[10000];
	for (int i = 0; i <= max_num + 1; i++)
		t[i] = 0;
	for (int i = 0; ;i++) {
		for (int now = 1; now <= cag; now++) {
			int epco = 0;
			while(1) {//最多循环100次,生成不了就算了
				int z1, z2;
				while (1) {
					z1 = rand() % max_num + 1;//找到两个没出现过的位置
					if (!t[z1]) {
						t[z1] = 1;
						break;
					}
				}
				while (1) {
					z2 = rand() % max_num + 1;
					if (!t[z2]) {
						t[z2] = 1;
						break;
					}
				}
				int x1 = (z1-1) % (N - 2)+1;
				int y1 = (z1 - 1) / (N - 2)+1;//计算行列
				int x2 = (z2-1) % (N - 2)+1;
				int y2 = (z2 - 1) / (N - 2)+1;

				int check = 0;
				ini_flag(y1,x1);
				dfs(x1, y1, x1, y1, x2, y2, 0,0,check);
				while (!st.empty())
					st.pop();
				if (check) {//有解
					mat[y1][x1] = now;
					mat[y2][x2] = now;
					break;
				}
				else {//无解
					epco++;
					t[z1] = 0;
					t[z2] = 0;
				}
				if (epco == 100)break;
			}
			if (epco == 100 || i * cag + now == max_num/2) {
				return i * cag + now-(epco==100);//循环一百次还没找到答案或者数量达到了,返回生成的组数
			}
		}
	}
}
```



## 结果显示

如果要消除了就要显示消除的样子，下面就主要介绍怎么显示。我单独开辟了一个矩阵tmp来暂存点阵，然后将刚才dfs过程中找到的点统统标记为-1，这时候存在的点是正数，不存在的是0，而负数就代表了消除路线所经过的方块。遍历数组，判断点阵情况，正数就显示数字，0不显示，负数直接把路径经过的点记为''.''。

```cpp
void show_mat(int mat[N][N]) {
	//for(int i=1;i<=N)
	printf("   ");
	for (int i = 1; i <= N - 2; i++)
		printf("%d ", i);
	printf("\n ");
	for (int i = 0; i < N ; i++)
	{
		if (i == N - 1)printf(" ");//调整最后一行点的格式
		if(i<=N-2&&i>=1)
			printf("%d", i);
		for (int j = 0; j < N ; j++) {
			if (mat[i][j] <0 ) {
				if(mat[i][j]==-1)
					printf(". ");//显示答案
				else
					printf(". ");//显示答案
			}
			else if (mat[i][j])
				printf("%d ", mat[i][j]);
			else
				printf("  ");//否则不显示
		}
		printf("\n");
	}
}
```



## 初级汇总

简单综合一下，加入一些用户交互，就可以实现一个非常简陋的数字点阵连连看，用户输入坐标实现选择。

下面是完整的代码

```cpp
#define _CRT_SECURE_NO_WARNINGS 1
#include<stdio.h>
#include<algorithm>
#include<queue>
#include<stack>
#include <stdlib.h>
#include <time.h>
#include<graphics.h>//图形绘制库
using namespace std;
const int N = 9+2; // 游戏界面加上隐藏一圈的大小,第一个数字是真正的游戏界面大小,1-9
const int cag = 9;//数字种类，1-9
const int inf = 0x3f3f3f3f;

int dirx[] = { 0,1,0,-1 };
int diry[] = { -1,0,1,0 };

struct node {
	int x,y,dir;//横坐标,纵坐标,方向(上下或者左右)
};

queue<node>q;
stack<node>st;

char a[N][N];
int flag[N][N];//次数
int mat[N][N];//游戏界面矩阵
int tmp[N][N];//暂存矩阵

void print() {
	for (int i = 0; i < N; i++) {
		for (int j = 0; j < N; j++)
			printf("%c ", a[i][j]);
		printf("\n");
	}
}

void ini_flag(int x,int y) {//相当于x-行,y-列
	for (int i = 0; i < N; i++)
		for (int j = 0; j < N; j++) {
			flag[i][j] = inf;
		}
	flag[x][y] = 0;
}

void all_ini() {
	for (int i = 0; i < N; i++)
		for (int j = 0; j < N; j++) {
			mat[i][j] = 0;
		}
}

void show_mat(int mat[N][N]) {
	//for(int i=1;i<=N)
	printf("   ");
	for (int i = 1; i <= N - 2; i++)
		printf("%d ", i);
	printf("\n ");
	for (int i = 0; i < N ; i++)
	{
		if (i == N - 1)printf(" ");//调整最后一行点的格式
		if(i<=N-2&&i>=1)
			printf("%d", i);
		for (int j = 0; j < N ; j++) {
			if (mat[i][j] <0 ) {
				if(mat[i][j]==-1)
					printf(". ");//显示答案
				else
					printf(". ");//显示答案
			}
			else if (mat[i][j])
				printf("%d ", mat[i][j]);
			else
				printf("  ");//否则不显示
		}
		printf("\n");
	}
}

void dfs(int x,int y, int qdx, int qdy,int zdx,int zdy,int step,int now,int &stats) {
	if (stats)return;//找到了就不找了
	//判断,参数分别是x,y,(x向右,y向下),起点xy和终点xy,当前转弯次数,当前方向参数，0123分别是上右下左,当前寻找的状态
	if (step == 3) {//超出步数或者障碍物
		if (!st.empty()) {//拿出去
			st.pop();
		}
		return;
	}
	if (zdx==x&&zdy==y&&step <= 2) {//终点
		stats = 1;
		st.pop();//终点不需要变成横线
		return;
	}
	for (int i = now; i < now + 4; i++) {//从当前方向开始找
		int index = i % 4;
		int xx = x + dirx[index];
		int yy = y + diry[index];
		if (i == now + 2&&x!=qdx&&y!=qdy || xx < 0 || yy < 0 || xx == N || yy == N||
			mat[yy][xx]&&!(xx==zdx&&yy==zdy))//不合法,最后一个代表已经被占据并且不是终点
			continue;
		if (now == index|| x == qdx && y == qdy) {//方向相同或者是起点
			flag[yy][xx] = min(flag[y][x], flag[yy][xx]);//不需要加转弯次数
			st.push({ xx,yy });
			dfs(xx, yy, qdx,qdy,zdx,zdy,step, index, stats);
		}
		else {
			flag[yy][xx] = min(flag[y][x] + 1, flag[yy][xx]);//需要加转弯次数
			st.push({ xx,yy,now%2 });
			dfs(xx, yy, qdx, qdy, zdx, zdy, step + 1, index, stats);
		}
		if (stats)return;
	}
	if (stats)return;
	if (!st.empty()) {//四个方向都没有
		st.pop();
	}
	return ;
}

int create_mat() {
	srand((unsigned)time(NULL));
	//生成N-2*N-2大小游戏矩阵,目前采用的算法是随机生成，可能最后不能填满...
	int max_num = (N - 2)*(N - 2);
	bool t[10000];
	for (int i = 0; i <= max_num + 1; i++)
		t[i] = 0;
	for (int i = 0; ;i++) {
		for (int now = 1; now <= cag; now++) {
			int epco = 0;
			while(1) {//最多循环100次,生成不了就算了
				int z1, z2;
				while (1) {
					z1 = rand() % max_num + 1;//找到两个没出现过的位置
					if (!t[z1]) {
						t[z1] = 1;
						break;
					}
				}
				while (1) {
					z2 = rand() % max_num + 1;
					if (!t[z2]) {
						t[z2] = 1;
						break;
					}
				}
				int x1 = (z1-1) % (N - 2)+1;
				int y1 = (z1 - 1) / (N - 2)+1;//计算行列
				int x2 = (z2-1) % (N - 2)+1;
				int y2 = (z2 - 1) / (N - 2)+1;

				int check = 0;
				ini_flag(y1,x1);
				dfs(x1, y1, x1, y1, x2, y2, 0,0,check);
				while (!st.empty())
					st.pop();
				if (check) {//有解
					mat[y1][x1] = now;
					mat[y2][x2] = now;
					break;
				}
				else {//无解
					epco++;
					t[z1] = 0;
					t[z2] = 0;
				}
				if (epco == 100)break;
			}
			if (epco == 100 || i * cag + now == max_num/2) {
				return i * cag + now-(epco==100);//循环一百次还没找到答案或者数量达到了,返回生成的组数
			}
		}
	}
}

void play(int all_num) {
	int x1, y1, x2, y2;
	while (all_num) {//只要还剩下
		printf("输入两个消除目标的坐标:");
		scanf("%d%d%d%d", &x1, &y1, &x2, &y2);
		if (mat[y1][x1] != mat[y2][x2]) {
			printf("请保证数字匹配\n");
			continue;
		}
		if (x1 == x2 && y1 == y2) {
			printf("不要输入同一个坐标\n");
			continue;
		}
		if (x1 > 0 && x1 <= N - 2 && y1 > 0 && y1 <= N - 2 && x2 > 0 && x2 <= N - 2 && y2 > 0 && y2 <= N - 2)
		{
			int check = 0;
			ini_flag(y1, x1);
			dfs(x1, y1, x1, y1, x2, y2, 0, 0, check);
			if (check) {
				for (int i = 0; i < N; i++)
					for (int j = 0; j < N; j++)
						tmp[i][j] = mat[i][j];
				while (!st.empty()) {
					if(st.top().dir%2==0)
						tmp[st.top().y][st.top().x] = -1;//表示上下
					else
						tmp[st.top().y][st.top().x] = -2;//表示左右
					st.pop();
				}
				printf("成功消除,消除情况如下\n");
				show_mat(tmp);
				all_num--;
				if (!all_num) {
					printf("恭喜你通关了");
					break;
				}
				mat[y1][x1] = 0;
				mat[y2][x2] = 0;
				printf("当前状态\n");
				show_mat(mat);
				printf("\n");
			}
			else {
				printf("不合法消除，重新输入\n");
			}
		}
		else {
			printf("请输入正确的坐标范围(1-%d)\n", N - 2);
		}
	}
}

void find_error() {//调试用的
	mat[1][3] = 4;
	mat[3][3] = 4;
	mat[1][1] = 2;
	mat[2][1] = 2;
	mat[3][1] = 1;
	mat[1][2] = 3;
	mat[2][2] = 1;
	mat[3][2] = 3;
	show_mat(mat);
	int check = 0;
	ini_flag(1, 1);
	dfs(3, 1, 3,1, 3, 3, 0, 0, check);
	for (int i = 0; i < N; i++)
		for (int j = 0; j < N; j++)
			tmp[i][j] = mat[i][j];
	while (!st.empty()) {
		printf("%d %d\n", st.top().x, st.top().y);
		if (st.top().dir % 2 == 0)
			tmp[st.top().y][st.top().x] = -1;//表示上下
		else
			tmp[st.top().y][st.top().x] = -2;//表示左右
		st.pop();
	}
	show_mat(tmp);
}

int main() {
	//set(); //调试用
	all_ini();//初始化所有矩阵
	int all_num = create_mat();//生成矩阵
	show_mat(mat);
	play(all_num);
	return 0;
}
```



# 连连看---正式程序

> 下面是真正的连连看开发，开发工具是VS2017。
>
> 基于上面几个算法，其实项目大体的结构已经有了。首先是**生成**，每个图片的种类其实就是一个编号，背后就是数字矩阵，先用以上生成规则去生成数字矩阵，然后利用数字找到对应图片，放到相应位置，而这个位置取决于相应的数字矩阵下标。**判定两张图片能否消除**其实也是判定相应的数字矩阵的数字能否匹配。**画出路径**主要取决于搜索过程中的过程栈存储的路径信息，这里还是比较容易晕。
>
> 还需要加入的功能主要包括交互，显示模块和提示模块。我这里使用的交互方式是鼠标交互，一开始是用的键盘，然而体验并不好；计时连击和提示的加入，主要是为了提高游戏体验感。

## 常量和全局变量

项目中会需要用到很多常数，为了方便查找最好放一起，我自己的项目结构如下，头文件head.h主要负责定义常量，一个主程序main.cpp定义入口，另一个tool.cpp则负责具体游戏的实现。



![img](https://s21.ax1x.com/2025/11/24/pZAkqNd.png)


常量定义

```cpp
const int N = 10 + 2; // 游戏界面加上隐藏一圈的大小,第一个数字是真正的游戏界面大小,1-6(当前适应大小)
const int cag = 7;//水果种类，1-7
const int inf = 0x3f3f3f3f;
#define SIZE 70 //图片尺寸
#define MAXX N*SIZE+400
#define MAXY N*SIZE
#define RIGHT 77
#define LEFT 75
#define UP 72
#define DOWN 80
#define ENTER 13
#define W 119
#define A 97
#define S 115
#define D 100
#define KONG 32
#define ESC 27//获取键值
#define EASY 130
#define NORMAL 90
#define DIFFICULT 70
#define DET 3 //连击间隔计算
```



 实现函数全局变量

```cpp
/*全局变量*/
struct node {
	int x, y, dir;//横坐标,纵坐标,方向(上下或者左右)
};

int dirx[] = { 0,1,0,-1 };
int diry[] = { -1,0,1,0 };
stack<node>st;//记录路径
IMAGE img[20];//存储图片
int all_num;//水果对数目
int rem_num;//消除个数
int flag[N][N];//次数,计算能否相连要用
int mat[N][N];//游戏界面矩阵,记录了每个位置的图片编号
int tmp[N][N];//暂存矩阵，标记线
//显示模块控制模块
clock_t start, stop;//clock_t为clock()函数返回的变量类型
double duration;//记录被测函数运行时间，以秒为单位
bool if_time;//标记是否计时
int max_num;//最高连击次数
int now_num;//当前连击次数
int k = (N - 2) / 2;//大小参数，为了调节不同大小适应的方阵距离
int kr=SIZE/5;//表示当前大小的方块对应的圆半径系数
int all_time;//可用初始时间
int time0;//游戏开始时间
int add_time = 1;//消除一个加1秒
```



## 初始化

作为一个项目，需要初始化的变量非常多，尤其是一些全局变量在一次游戏之内只需要初始化一次，最好把需要初始化的全部放到一个函数内封装起来，这样以后需要加入也会非常方便。

在这里我的初始化主要包括对于mat矩阵的清零操作，初始化一些全局变量，同时载入图片，放进图片数组。

```cpp
/*初始化mat矩阵和图片以及计时模块*/
void all_ini() {
	for (int i = 0; i < N; i++) {
		for (int j = 0; j < N; j++) {
			mat[i][j] = 0;
		}
	}
	if_time = 0;//一开始的不需要计时
	max_num = 1;
	now_num = 1;
	all_time = 100;
	time0 = time(0);
	loadimage(&img[1], "./imgs/西瓜.png");
	loadimage(&img[2], "./imgs/苹果.png");
	loadimage(&img[3], "./imgs/草莓.png");
	loadimage(&img[4], "./imgs/梨.png");
	loadimage(&img[5], "./imgs/火龙果.png");
	loadimage(&img[6], "./imgs/桃子.png");
	loadimage(&img[7], "./imgs/香蕉.png");
	loadimage(&img[10], "./imgs/竖线.png");
	loadimage(&img[11], "./imgs/横线.png");
	loadimage(&img[12], "./imgs/下右.png");
	loadimage(&img[13], "./imgs/下左.png");
	loadimage(&img[14], "./imgs/上右.png");
	loadimage(&img[15], "./imgs/上左.png");
}//初始化mat矩阵和图片
```



## 生成--判定--显示

生成和判定其实跟番外的那个是一样的，生成生成的还是数字，判定消除也是一样的，因为消除的虽然是图片，查找还是针对数字，没有任何变化，我自己也是直接复制了番外的代码，没做什么修改，就不多展开描述。

生成图片部分略有变化，这时候针对传入的数组矩阵，如果是正数那么**需要放入对应下标的图片**，要记得乘上图片大小；如果是负数的话，那么就是路径点，分为多种情况，下文做考虑；如果是0那就是空，不显示。

## 用户操作

> 下面这里就是开发过程中的最大难点，也是联系最多程序的地方，也就是用户操作的编写，涉及到读取用户信息并处理。

首先分析一下思路，用户面对一个连连看界面，会有两种操作，分别是键盘和鼠标的操作。

### 鼠标

首先是鼠标移动，也就是定位，然后选择水果并点击。

第一次选择时会在这个水果上面打上一个标记，同时状态转变（标记是第一个水果还是第二个水果的状态），在这之后可能会再点击，再次点击可能是空，也可能还是这个水果，这两种情况直接跳过，也就是判定为无效操作，同时清除标记，状态转变；再次点击还有可能是其他水果，这时候就要详细讨论，如下。

![img](https://s21.ax1x.com/2025/11/24/pZAkj3t.png)

**1.可以成功消除**

如果可以消除，那么这时候就要显示出消除线，同时清除两个水果，清除两个水果比较简单，只要在对应的数字矩阵中将对应位置的数字置为0，重新显示就是消除之后的样子，画出消除线相对复杂。

确定当前线now的方向需要前后两个矩阵元素信息，上面说了判定消除实际上可以返回一个栈，细化来看，这个栈实际上存储了从起点开始一直到终点的所有路径坐标以及对应的方向信息（也就是横线还是竖线），由于栈后进先出的的特点，一开始的栈顶实际上就是终点的方向信息，所以现在相当于是反恢复操作，将终点元素记录为bef（before），然后开始向下一个栈元素查找。观察下图可知，**只有在前一个元素bef和自身元素now方向不相同时，这时才会有折线产生**，而具体向哪边弯曲就取决于下一个元素，如果在上面，那就是向上折，向下就是向下折，而左右取决于bef元素在哪边，其他的各种情况同理，横线和竖线几乎相反，需要细分考虑，将所有情况都考虑到，然后给每个位置标上相应的线型编号，在显示时可以用到。

![img](https://s21.ax1x.com/2025/11/24/pZAkvgP.png)

>  在观察了这个图之后，发现可能还是放在队列中会更好恢复，直接从起点开始恢复，遇到当前线型和下一个不一样，就看下一个在哪边，就往哪边弯曲。

**2.不能成功消除**

这种情况比较好考虑，考虑游戏的实际情况，这时候可以将标记点清除，切换回选第一个目标的状态。

### 键盘

然后是关于键盘的操作。关于键盘的操作可以有很多种，我主要设计了两种，分别是暂停和提示。

暂停非常好理解，就是点击了之后就会停止程序。下面介绍提示模块。

**提示**

关于提示，就是直接搜索数字矩阵，枚举寻找可以消除的数字，然后返回其坐标，利用画线函数将两个方块的路径标记好，显示出来，为了真实，我将其显示的时间设置为了1秒，然后就会消失，还可以加入次数限制，我还没有加入。

提示设计的难度不大，但是经过测试有时候可能难以达到实时水平，尤其是上面空旷的时候，由于搜索是优先向上，空旷就会导致搜索次数上升很快，导致卡顿，所以慎用。

## 显示模块

不同于前面的显示，这里的显示主要针对于一些游戏参数显示到屏幕上，比如连击信息，剩余时间，剩余数目等，同时还要考虑到全部消除和时间耗尽这两种结束显示。

**连击和剩余数目**

剩余数目很好理解，根据一般的游戏逻辑，连击就是上一次消除和这一次消除之间的间隔少于几秒再次消除就是连击，所以我们需要**在上一次消除之后开始计时，然后等到再下一次消除看是否小于这个时间间隔**，是那就给连击+1，否则重置连击数，这两个与消除息息相关，所以在每次成功消除之后调用该函数。

**代码中的deal就是把一个数字转为字符串然后存到a中，因为outtextxy只能显示字符串。**

```cpp
/*计算和统计模块*/
void printCal() {
	char a[100];

	//计算连击
	settextcolor(WHITE);
	settextstyle(40, 40, 0);
	outtextxy(N*SIZE+40, 100, "当前连击");
	settextstyle(100, 100, 0);
	deal(now_num, a);
	outtextxy(N*SIZE+170, 200, a);

	//统计数目
	settextcolor(WHITE);
	settextstyle(30, 30, 0);
	outtextxy(N*SIZE+40, N*SIZE-250, "剩余水果对");
	settextstyle(100, 100, 0);
	deal(all_num, a);
	outtextxy(N*SIZE + 150, N*SIZE - 170, a);
}//计算连击和统计模块
```



**剩余时间**

剩余时间这里采纳了同学的建议，采用这样的计时方式：初始有一个时间长度，然后每次消除可以增加一定时间。

为了达到这样的效果，引入初始时间长度all_time，消除增加时间det_time和消除个数rem_num,同时记录开始时间time0，利用time(0)-time0即可得到从游戏开始到目前经过的时长，剩余时长可表达为下式:

**all_time+(rem_num\*det_time)-(time(0)-time0)**

其中rem_num*det_time就是增加的时长，time(0)-time0就是游戏经过的时长。

这个需要一直更新，所以放在用户操作的主程序中调用。

```cpp
/*打印时间*/
void printTime() {
	char a[100];
	//剩余时间
	settextcolor(WHITE);
	settextstyle(40, 40, 0);
	outtextxy(N*SIZE + 40, 300, "剩余时间");
	settextstyle(100, 100, 0);
	int t = all_time + (rem_num*add_time) - (time(0) - time0);
	deal(t, a);
	if (t % 10 == 9) {
		clearrectangle(N*SIZE + 170, 400, N*SIZE + 400, 500);
	}
	outtextxy(N*SIZE + 170, 400, a);
}//打印时间
```



**游戏结束**

游戏结束有两种情况，一种是时间耗尽，这种算是游戏失败，还有一种是全部消除，这种是游戏成功，成功的时候还可以打印出最高连击次数，比较简单，就不展开。

**到这里程序主体完毕。**

## ****完整代码****

**head.h**

```cpp
#define _CRT_SECURE_NO_WARNINGS 1
#include<stdio.h>
#include<algorithm>
#include<stack>
#include <stdlib.h>
#include <time.h>
#include<graphics.h>//图形绘制库
using namespace std;

const int N = 10 + 2; // 游戏界面加上隐藏一圈的大小,第一个数字是真正的游戏界面大小,1-6(当前适应大小)
const int cag = 7;//水果种类，1-7
const int inf = 0x3f3f3f3f;
#define SIZE 70 //图片尺寸
#define MAXX N*SIZE+400
#define MAXY N*SIZE
#define RIGHT 77
#define LEFT 75
#define UP 72
#define DOWN 80
#define ENTER 13
#define W 119
#define A 97
#define S 115
#define D 100
#define KONG 32
#define ESC 27//获取键值
#define EASY 130
#define NORMAL 90
#define DIFFICULT 70
#define DET 3 //连击间隔计算

void game();//游戏函数入口
```



 **main.cpp**

```cpp
#include"head.h"
#include<graphics.h>//图形绘制库

int main() {
	initgraph(MAXX + 1, MAXY + 1);
	game();
	return 0;
}
```



 **tool.cpp**

```cpp
#include"head.h"
#include"conio.h"

/*全局变量*/
struct node {
	int x, y, dir;//横坐标,纵坐标,方向(上下或者左右)
};

int dirx[] = { 0,1,0,-1 };
int diry[] = { -1,0,1,0 };
stack<node>st;//记录路径
IMAGE img[20];//存储图片
int all_num;//水果对数目
int rem_num;//消除个数
int flag[N][N];//次数,计算能否相连要用
int mat[N][N];//游戏界面矩阵,记录了每个位置的图片编号
int tmp[N][N];//暂存矩阵，标记线
//显示模块控制模块
clock_t start, stop;//clock_t为clock()函数返回的变量类型
double duration;//记录被测函数运行时间，以秒为单位
bool if_time;//标记是否计时
int max_num;//最高连击次数
int now_num;//当前连击次数
int k = (N - 2) / 2;//大小参数，为了调节不同大小适应的方阵距离
int kr=SIZE/5;//表示当前大小的方块对应的圆半径系数
int all_time;//可用初始时间
int time0;//游戏开始时间
int add_time = 1;//消除一个加1秒


/*初始化mat矩阵和图片以及计时模块*/
void all_ini() {
	for (int i = 0; i < N; i++) {
		for (int j = 0; j < N; j++) {
			mat[i][j] = 0;
		}
	}
	if_time = 0;//一开始的不需要计时
	max_num = 1;
	now_num = 1;
	all_time = 30;
	rem_num = 0;
	time0 = time(0);
	loadimage(&img[1], "./imgs/西瓜.png");
	loadimage(&img[2], "./imgs/苹果.png");
	loadimage(&img[3], "./imgs/草莓.png");
	loadimage(&img[4], "./imgs/梨.png");
	loadimage(&img[5], "./imgs/火龙果.png");
	loadimage(&img[6], "./imgs/桃子.png");
	loadimage(&img[7], "./imgs/香蕉.png");
	loadimage(&img[10], "./imgs/竖线.png");
	loadimage(&img[11], "./imgs/横线.png");
	loadimage(&img[12], "./imgs/下右.png");
	loadimage(&img[13], "./imgs/下左.png");
	loadimage(&img[14], "./imgs/上右.png");
	loadimage(&img[15], "./imgs/上左.png");
}//初始化mat矩阵和图片


/*初始化flag*/
void ini_flag(int x, int y) {//相当于x-行,y-列
	for (int i = 0; i < N; i++)
		for (int j = 0; j < N; j++) {
			flag[i][j] = inf;
		}
	flag[x][y] = 0;
}//初始化flag

/*清除圆并补齐画面*/
void clearRound(int x,int y,int r,int detx,int dety) {//分别是xy点坐标，半径，偏移量
	int real_x, real_y;
	real_x = x * SIZE + SIZE / 2+detx;
	real_y = y * SIZE + SIZE / 2+dety;
	clearcircle(real_x, real_y, r);
	if(mat[y][x])
		putimage(x*SIZE, y*SIZE, &img[mat[y][x]]);//补上图片
}//清除圆并补齐画面

/*画一个圆*/
void drawRound(int x,int y,int r,int detx,int dety,int color){
	int real_x = x * SIZE + SIZE / 2;//圆心坐标
	int real_y = y * SIZE + SIZE / 2;
	circle(real_x + detx, real_y + dety, r);
	setfillcolor(color);  //填充色为蓝色
	fillcircle(real_x+detx, real_y +dety, r);
}//画一个圆

/*转换数字为字符串*/
void deal(int x, char *a) {
	stack <int>sst;
	while (x) {
		sst.push(x % 10);
		x /= 10;
	}
	int l = 0;
	while (!sst.empty()) {
		a[l++] = sst.top() + ''0'';
		sst.pop();
	}
	a[l] = ''\0'';
	if (a[0] == ''\0'') {
		a[0] = ''0'';
		a[1] = ''\0'';
	}
}//转换数字为字符串

/*计算和统计模块*/
void printCal() {
	char a[100];

	//计算连击
	settextcolor(WHITE);
	settextstyle(40, 40, 0);
	outtextxy(N*SIZE+40, 100, "当前连击");
	settextstyle(100, 100, 0);
	deal(now_num, a);
	outtextxy(N*SIZE+170, 200, a);

	//统计数目
	settextcolor(WHITE);
	settextstyle(30, 30, 0);
	outtextxy(N*SIZE+40, N*SIZE-250, "剩余水果对");
	settextstyle(100, 100, 0);
	deal(all_num, a);
	outtextxy(N*SIZE + 150, N*SIZE - 170, a);
}//计算连击和统计模块

/*时间耗尽*/
void print_timeOver() {
	//打印时间耗尽
	settextcolor(BROWN);
	int size = k * 150 / (N - 2);//字体大小
	settextstyle(size, size, 0);
	outtextxy(N*SIZE / 2 - 4 * size, N*SIZE / 2 - 2 * size, "时间耗尽");
}//时间耗尽

/*打印时间*/
void printTime() {
	char a[100];
	//剩余时间
	settextcolor(WHITE);
	settextstyle(40, 40, 0);
	outtextxy(N*SIZE + 40, 300, "剩余时间");
	settextstyle(100, 100, 0);
	int t = all_time + (rem_num*add_time) - (time(0) - time0);
	deal(t, a);
	if (t % 10 == 9) {
		clearrectangle(N*SIZE + 170, 400, N*SIZE + 400, 500);
	}
	outtextxy(N*SIZE + 170, 400, a);
}//打印时间

/*结束*/
void printOver() {
	//打印消除完毕
	settextcolor(BROWN);
	int size = k*150 / (N - 2);//字体大小
	settextstyle(size, size, 0);
	outtextxy(N*SIZE/2-4*size, N*SIZE / 2 - 2*size, "全部消除");
	line(size, N*SIZE / 2 - size/2, N*SIZE - size, N*SIZE / 2 -  size/2);
	//打印最高连击
	char a[100];
	settextcolor(BLUE);
	size = k*90 / (N - 2);
	settextstyle(size, size, 0);
	outtextxy(N*SIZE / 2 -6 * size, N*SIZE / 2 , "最高连击次数");
	size = k*150 / (N - 2);
	settextstyle(size, size, 0);
	deal(max_num, a);
	outtextxy(N*SIZE / 2 -size/2, N*SIZE / 2+size, a);
	system("pause");
}//结束

/*打印当前状态*/
void show_mat(int Mat[N][N]) {
	printCal();
	line(N*SIZE, 0, N*SIZE, MAXY);
	for (int i = 0; i <= N - 1; i++) {
		for (int j = 0; j <= N - 1; j++) {
			if (Mat[i][j] < 0) {
				putimage(j*SIZE, i*SIZE, &img[-Mat[i][j]+9]);
			}
			else if (Mat[i][j] == 0)
				continue;
			else
				putimage(j*SIZE, i*SIZE, &img[Mat[i][j]]);
		}
	}
}//打印当前状态

/*搜索答案*/
void dfs(int x, int y, int qdx, int qdy, int zdx, int zdy, int step, int now, int &stats) {
	if (stats)return;//找到了就不找了
	//判断,参数分别是x,y,(x向右,y向下),起点xy和终点xy,当前转弯次数,当前方向参数，0123分别是上右下左,当前寻找的状态
	if (step == 3) {//超出步数
		if (!st.empty()) {//拿出去
			st.pop();
		}
		return;
	}
	if (zdx == x && zdy == y && step <= 2) {//终点
		stats = 1;
		return;
	}
	for (int i = now; i < now + 4; i++) {//从当前方向开始找
		int index = i % 4;
		int xx = x + dirx[index];
		int yy = y + diry[index];
		if (i == now + 2 && x != qdx && y != qdy || xx < 0 || yy < 0 || xx == N || yy == N ||
			mat[yy][xx] && !(xx == zdx && yy == zdy))//不合法,最后一个代表已经被占据并且不是终点
			continue;
		if (now == index || x == qdx && y == qdy) {//方向相同或者是起点
			flag[yy][xx] = min(flag[y][x], flag[yy][xx]);//不需要加转弯次数
			st.push({ xx,yy,i % 2 });
			dfs(xx, yy, qdx, qdy, zdx, zdy, step, index, stats);
		}
		else {
			flag[yy][xx] = min(flag[y][x] + 1, flag[yy][xx]);//需要加转弯次数
			st.push({ xx,yy,i % 2 });
			dfs(xx, yy, qdx, qdy, zdx, zdy, step + 1, index, stats);
		}
		if (stats)return;
	}
	if (stats)return;
	if (!st.empty()) {//四个方向都没有
		st.pop();
	}
	return;
}//搜索答案

/*生成矩阵*/
int create_mat(int max_num) {
	srand((unsigned)time(NULL));
	//生成N-2*N-2大小游戏矩阵,目前采用的算法是随机生成，可能最后不能填满...
	bool t[10000];
	for (int i = 0; i <= max_num + 1; i++)
		t[i] = 0;
	for (int i = 0; ; i++) {
		for (int now = 1; now <= cag; now++) {
			int epco = 0,jh=0;
			while (1) {//最多循环100次,生成不了就算了
				int z1, z2;
				while (1) {
					z1 = rand() % max_num + 1;//找到两个没出现过的位置
					if (!t[z1]) {
						t[z1] = 1;
						break;
					}
				}
				while (1) {
					z2 = rand() % max_num + 1;
					if (!t[z2]) {
						t[z2] = 1;
						break;
					}
				}

				int x1 = (z1 - 1) % (N - 2) + 1;
				int y1 = (z1 - 1) / (N - 2) + 1;//计算行列
				int x2 = (z2 - 1) % (N - 2) + 1;
				int y2 = (z2 - 1) / (N - 2) + 1;

				int check = 0;
				ini_flag(y1, x1);
				dfs(x1, y1, x1, y1, x2, y2, 0, 0, check);
				while (!st.empty())
					st.pop();
				if (check) {//有解
					mat[y1][x1] = now;
					mat[y2][x2] = now;
					break;
				}
				else {//无解
					epco++;
					t[z1] = 0;
					t[z2] = 0;
				}
				if (epco == 100 && t[z1] == 0) {
					jh = 1;//表示是退出的,而不是刚好在100轮生成
					break;
				}
			}
			if (jh || i * cag + now == max_num / 2- max_num%2) {
				show_mat(mat);
				return i * cag + now - jh;//循环一百次还没找到答案或者数量达到了,返回生成的组数
			}
		}
	}
}//生成矩阵

/*标记线*/
void deal(int x1,int x2,int y1,int y2) {
	for (int i = 0; i < N; i++)
		for (int j = 0; j < N; j++)
			tmp[i][j] = mat[i][j];//复制矩阵
	int bef_dir = st.top().dir;//初始方向
	int bef_x = st.top().x;
	int bef_y = st.top().y;
	st.pop();//终点不需要变成线
	while (!st.empty()) {
		int xx = st.top().x;
		int yy = st.top().y;
		int dir = st.top().dir;
		st.pop();
		if (dir == 0) {//上下
			//tmp[st.top().y][st.top().x] = -1;//表示上下
			if (bef_dir==dir) {
				//跟上一个一样
				tmp[yy][xx] = -1;//上下
			}
			else {         //左右
				if (!st.empty() && st.top().y < yy || st.empty() && y1 < yy) {
					//跟下一个比定上下,此时是下一个在上面
					if (xx < bef_x)//向左走
						tmp[yy][xx] = -5;//上右
					else
						tmp[yy][xx] = -6;//上左
				}
				else {
					if (xx < bef_x)//向左走
						tmp[yy][xx] = -3;//下右
					else
						tmp[yy][xx] = -4;//下左
				}
			}
		}
		else {
			//tmp[st.top().y][st.top().x] = -2;//表示左右
			if (bef_dir == dir) {//也是左右
				tmp[yy][xx] = -2;
			}
			// x1 y1 是起点坐标
			else {
				if (!st.empty() && st.top().x < xx || st.empty() && x1 < xx) {//下一个在左边
					if (yy < bef_y) //向上走
						tmp[yy][xx] = -4;//下左
					else
						tmp[yy][xx] = -6;//上左
				}
				else {
					if (yy < bef_y)//向上走
						tmp[yy][xx] = -3;//下右
					else
						tmp[yy][xx] = -5;//上右
				}
			}
		}
		bef_x = xx;
		bef_y = yy;
		bef_dir = dir;//更新表示上一个状态的量
	}
}//标记线

/*处理消除之后的样子以及计时模块*/
bool deal_after(int x1,int x2,int y1,int y2) {
	//计时模块
	rem_num++;
	all_num--;
	if (if_time) {
		stop = clock();//停止计时
		duration = (double)(stop - start) / CLOCKS_PER_SEC;//计算运行时间
		if (duration <= DET) {
			now_num += 1;
			max_num = max(max_num, now_num);
		}
		else {
			now_num = 1;//重置连击次数
		}
	}
	//下面显示出消除的样子
	show_mat(tmp);
	Sleep(300);//程序暂停
	mat[y1][x1] = 0;
	mat[y2][x2] = 0;
	cleardevice();
	show_mat(mat);
	if (!all_num) {
		return 1;//代表可以结束游戏了
	}
	start = clock();//开始计时
	if_time = 1;//计时标记
	return 0;
}

/*提示模块*/
bool remind(int &x1, int &y1, int &x2, int &y2) {//返回是否可以消除,返回值内包括起点终点坐标
	for (int i1 = 1; i1 < N - 1; i1++) {
		for (int j1 = 1; j1 < N - 1; j1++) {
			if (!mat[i1][j1])
				continue;//空格
			for (int i2 = 1; i2 < N - 1; i2++) {
				for (int j2 = 1; j2 < N - 1; j2++) {
					if (!mat[i2][j2] || i1 == i2 && j1 == j2||mat[i2][j2]!=mat[i1][j1])
						continue;
					int check = 0;
					dfs(j1, i1, j1, i1, j2, i2, 0, 0, check);
					if(check) {
						x1 = j1;
						y1 = i1;
						x2 = j2;
						y2 = i2;
						return 1;
					}
					else {
						while (!st.empty())//否则要清空栈
							st.pop();
					}
				}
			}
		}
	}
	return 0;
}//提示模块

/*游戏操作部分*/
void play() {
	int x1=1, y1=1, x2=1, y2=1,now=1;//记录当前位置(x2,y2)和上一个位置(x1,y1),now记录当前状态，1是第一个，2是第二个
	int bef_t = time(0)-time0;
	while (all_num) {//只要还剩下
		MOUSEMSG m;
		if (bef_t != time(0) - time0) {
			printTime();
		}
		printTime();//打印时间
		if (all_time + (rem_num*add_time) - (time(0) - time0)==0) {//时间耗尽
			print_timeOver();
			system("pause");
		}
		if (MouseHit())//是否有鼠标消息
		{
			m = GetMouseMsg();
			if (m.uMsg == WM_LBUTTONDOWN)//左键
			{
				//rectangle(m.x - 5, m.y - 5, m.x + 5, m.y + 5);
				if (m.x > N*SIZE || m.y > N*SIZE)
					continue;
				m.x = m.x / SIZE;
				m.y = m.y / SIZE;
				if (now == 1) {//第一个确定状态只要不越界都可以,然后记录上一个位置
					x1 = m.x;
					y1 = m.y;//x1，y1存储上一次状态
					if (!mat[y1][x1])
						continue;//空格无效
					drawRound(x1, y1, kr, 0, 0, GREEN);
					now = 2;
				}
				else {//第二个要判定状态
					x2 = m.x;
					y2 = m.y;
					if (mat[y1][x1] != mat[y2][x2] || x1 == x2 && y1 == y2) {
						clearRound(x1, y1, kr, 0, 0);//重新选择
						now = 1;
						continue;
					}
					if (x1 > 0 && x1 <= N - 2 && y1 > 0 && y1 <= N - 2 && x2 > 0
						&& x2 <= N - 2 && y2 > 0 && y2 <= N - 2) {
						int check = 0;
						ini_flag(y1, x1);
						dfs(x1, y1, x1, y1, x2, y2, 0, 0, check);
						if (check) {
							deal(x1, x2, y1, y2);//标记线
							bool if_over = deal_after(x1, x2, y1, y2);//判断是否结束，同时善后工作
							if (if_over) {
								printOver();//打印结束语句
							}
						}
						else {
							clearRound(x1, y1, kr, 0, 0);//不能消除
							while (!st.empty())//清空栈
								st.pop();
						}
					}
					now = 1;
				}
			}
		}
		if (_kbhit()) {//如果有按键
			int ch = _getch();
			if (ch == KONG) {//提示按钮
				int check = remind(x1,y1,x2,y2);
				if (check) {
					deal(x1, x2, y1, y2);//标记线
					show_mat(tmp);
					Sleep(1000);
					cleardevice();
					show_mat(mat);
				}
			}
			else if (ch == ESC) {//暂停
				system("pause");
			}
		}
	}
}//游戏操作部分

/*总控程序*/
void game() {
	all_ini();//初始化所有矩阵和图片库
	all_num = create_mat((N - 2)*(N - 2));//生成矩阵
	show_mat(mat);
	play();
}//总控程序
```



# 总结

程序本身还有许多很多可以改进的地方，比如有时候显示消除线会错位，都到这个份上了还有什么好修改的，有时间还是需要修改一下，还有在遇到无法消除时的刷新，同时还想加入一些类似于关卡的模式，等有空再说吧。',0,'2022-09-09 19:12:32','2022-09-09 19:12:32');


INSERT INTO article_detail(article_id,version,content,deleted,create_time,update_time) VALUES(2,1,_utf8mb4'> 没想到这么快就又有时间了，好久不见，正好没什么事做把上一篇提到的想要实现的功能给实现了，顺便也修复了上篇文章提到的一个小bug，就作为补充吧。

**目录**

[修复](#修复)

[优化](#优化)

[新增](#新增)

[暂停](#暂停)

[刷新](#刷新)

[无法消除刷新 ](#无法消除刷新 )

[关卡模式 ](#关卡模式 )

[难度参数](#难度参数)

[关卡切换](#关卡切换)

[关卡动画](#关卡动画)

[效果图 ](#效果图 )

------



# 修复

首先是修复问题，在之前老的项目上跑，最大的问题就是会出现一种奇怪的画线，也就是跑过头了，总之就是画线不对，但是判定两个能否消除却又从来没有错过，初步的判断是因为入栈的问题，逐步调试之后终于找到了问题所在。

这是在dfs里的一句代码，比较长，大概就是在确定了下一步之后要判定能不能这么走，后面几个条件是判定越界同时不能走到有数字的点除非是终点，没有问题，问题就出现在第一个条件，也就是限制不能往来的方向的反向方向走，除非是起点，当时我写的是下面这个

```cpp
if (i == now + 2 && x != qdx && y != qdy || xx < 0 || yy < 0 || xx == N || yy == N ||
			mat[yy][xx] && !(xx == zdx && yy == zdy))//不合法,最后一个代表已经被占据并且不是终点
			continue;
```



对比就会发现第一个条件不一样，实际上第一个条件是为了显限制不能往回走，除非是起点，因为任何时候对于普通的位置，你dfs转了一圈回到了自己相反的方向走回去是没有意义的，这样除了增加两个转弯什么都没有干，所以需要把这种情况判掉，但是起点不一样，从上面走下来，还可以重新出发从下面走，因为起点无所谓方向，而判定一个点是不是起点，应该是x值或者y值有任何一个和起点不一样就不是起点，改正之后如下。

```cpp
	if (i == now + 2 && (x != qdx || y != qdy) || xx < 0 || yy < 0 || xx == N || yy == N || // 第一个的逻辑是不能往回走，除非是起点
			mat[yy][xx] && !(xx == zdx && yy == zdy))//不合法,最后一个代表已经被占据并且不是终点
			continue;
```



#  优化

下面是一个小优化，之前跑一直没注意，原来dfs里面的flag数组压根没用，所以干脆就直接删掉了，代码整体看着也会清爽一点。

同时也增加了一个条件，在搜索时如果方向时，终点在左，那就不会往右边搜索，左右同理，这样可以有效避免一些无效搜索。

**修改后的dfs如下**

```cpp
/*搜索答案*/
void dfs(int x, int y, int qdx, int qdy, int zdx, int zdy, int step, int now, int &stats) {
	if (stats)return;//找到了就不找了
	//判断,参数分别是x,y,(x向右,y向下),起点xy和终点xy,当前转弯次数,当前方向参数，0123分别是上右下左,当前寻找的状态
	if (step == 3) {//超出步数
		if (!st.empty()) {//拿出去
			st.pop();
		}
		return;
	}
	if (zdx == x && zdy == y && step <= 2) {//终点
		stats = 1;
		return;
	}
	for (int i = now; i < now + 4; i++) {//从当前方向开始找
		int index = i % 4;
		int xx = x + dirx[index];
		int yy = y + diry[index];
		if (i == now + 2 && (x != qdx || y != qdy) || xx < 0 || yy < 0 || xx == N || yy == N || // 第一个的逻辑是不能往回走，除非是起点
			mat[yy][xx] && !(xx == zdx && yy == zdy))//不合法,最后一个代表已经被占据并且不是终点
			continue;
		if ((x != qdx || y != qdy) && (i % 2 == 0 && (zdx - qdx)*(xx - x) < 0 || i % 2 == 1 && (zdy - qdy)*(yy - y) > 0))//竖着走和横着走不可能偏离终点
			continue;
		if (now == index || x == qdx && y == qdy) {//方向相同或者是起点
			st.push({ xx,yy,i % 2 });
			dfs(xx, yy, qdx, qdy, zdx, zdy, step, index, stats);
		}
		else {
			st.push({ xx,yy,i % 2 });
			dfs(xx, yy, qdx, qdy, zdx, zdy, step + 1, index, stats);
		}
		if (stats)return;
	}
	if (stats)return;
	if (!st.empty()) {//四个方向都没有
		st.pop();
	}
	return;
}//搜索答案
```



# 新增

这里就是单独说一下增加的几个新功能，分别是刷新和暂停，还有自动刷新。

## 暂停

暂停就是点击之后游戏会停止，但是由于连连看的特殊性，如果仅仅是将程序停止，那么玩家可能就会在这时候看需要消除哪个，那么游戏的乐趣也会失去，因为这样就永远不存在找不到的时候。所以我考虑在暂停的时候将屏幕遮挡。那么执行逻辑就是**读取ESC按键---遮挡屏幕---程序暂停---再次点击任意键---恢复游戏**，点击任意键是因为C自带的程序暂停函数点击任意键恢复，遮挡屏幕的逻辑就是清屏，然后放上暂停图片，程序再次执行就把原来的东西重新显示即可。

有一个比较坑的地方就是关于计时，因为在暂停的时候time(0)是不会停止的，这个始终是真实的时间，而我们是把time(0)-time0当做游戏时间，所以为了防止暂停之后一会回到游戏直接时间耗尽，等效于time0再加上暂停的时间。

**代码如下**

```cpp
	   else if (ch == ESC) {
			time0+=stop_game();//要把计时起点向后挪动
			cleardevice();
			show_mat(mat);
		}
```



```cpp
/*游戏暂停*/
int stop_game() {
	int stop_time = time(0);
	cleardevice();
	putimage(MAXX / 2 + 100, MAXY / 2 - 100, &img[27]);
	system("pause");//停止
	return time(0)-stop_time;//返回暂停时间
}//游戏暂停
```



## 刷新

刷新也就是在无法消除的时候点击，然后水果会重新排列，我想的比较简单，就是如果按下了，那么就开始从头开始枚举，**找到了水果并且这个水果不是其他水果放过来的**（也就是一个水果只会被重置一次），那么就找个空位插进去，从头到尾走一遍，就实现了重新排列。

**代码如下**

flag在这里就是标记这个水果是不是已经被重置过。

```cpp
/*刷新模块*/
void refresh() {
	int x, y;
	ini_flag(0, 0, 0);//初始化flag为0，借用flag数组标记
	for (int i = 1; i <= N - 1; i++) {
		for (int j = 1; j <= N - 1; j++) {
			if (mat[i][j]&&!flag[i][j]) {//如果里面是有数字的并且不是前面的移过来的
				while (1) {
					x = rand() % (N - 2) + 1;
					y = rand() % (N - 2) + 1;//找到两个没出现过的位置
					if (!mat[y][x]) {//空的
						mat[y][x] = mat[i][j];
						mat[i][j] = 0;
						flag[y][x] = 1;//标记点
						break;
					}
				}
			}
		}
	}
	cleardevice();
	show_mat(mat);
}//刷新模块
```



## 无法消除刷新

这个主要就是关注到可能就是在一些情况下，会有无法消除的情况，这时候自己也很难看出，就需要系统自动刷新，就是结合一下提示和刷新两个代码就好，在每一次消除之后检测。

```cpp
int check = remind(x1, y1, x2, y2);
if (!check) {
	refresh();//没有可以消除的了,刷新
}
else {
	while (!st.empty())
	st.pop();
}
```



# 关卡模式

下面就是关卡模式的开发部分，关卡模式的每一关是不一样的，同时难度也需要慢慢递进，不可能针对每一关去设计独特的关卡，所以需要将控制每一关难度的参数单独抽离出来，同时也要考虑游戏间的衔接。

## 难度参数

从每一关来考量，每一关的难度大致由以下五个参数决定：水果数量，水果种类，消除可以增加的时间，游戏的初始时长，最后是提示消耗时长，为了方便游戏的调整，我将其放入了数组中，这样走循环就可以方便取出其中的难度组合。

```cpp
//关卡参数
int all_time;//可用初始时间
int add_time;//消除一个加1秒
int remind_price;//提示一次需要一秒代价
int all_num;//水果对数目
int cag;//水果种类，1-7
//下面是关卡参数配置
int eve_num[] = {1, 30, 35, 40, 45, 50,60 };//每一关的水果组数
int eve_cag[] = { 1, 8, 10, 12, 13, 14 ,14};//每一关的种类
int eve_start_time[] = { 100,15,15,20,20,30,30 };//每一关的初始时间
int eve_add_time[] = { 100,3,3,2,2,1 ,1};//每一关的消除加成时间
int eve_remind_price[] = { 1, 1, 1, 1, 2, 2 ,2 };//提示代价
```



在每一关的初始化中，我们只需要取出对应的参数,gk就是关卡，是传入的参数，对应的是关卡参数的下标，可以看到图片变多了，这是因为前面的那个涂片种类太少，难度也不好设置，所以就新加了一些。

```cpp
/*初始化*/
void all_ini(int gk) {//传入关卡值
	for (int i = 0; i < N; i++) {
		for (int j = 0; j < N; j++) {
			mat[i][j] = 0;
		}
	}
	//默认配置
	all_num = (N - 2)*(N - 2)/2;//最多生成的组数
	if_time = 0;//一开始的不需要计时
	time0 = time(0);//开始时间
	rem_num = 0;//消除数量
	max_num = 1;//最大连击
	now_num = 1;//当前连击
	loadimage(&img[1], "./imgs/西瓜.png");
	loadimage(&img[2], "./imgs/苹果.png");
	loadimage(&img[3], "./imgs/草莓.png");
	loadimage(&img[4], "./imgs/梨.png");
	loadimage(&img[5], "./imgs/火龙果.png");
	loadimage(&img[6], "./imgs/桃子.png");
	loadimage(&img[7], "./imgs/香蕉.png");
	loadimage(&img[8], "./imgs/橙子.png");
	loadimage(&img[9], "./imgs/橘子.png");
	loadimage(&img[10], "./imgs/荔枝.png");
	loadimage(&img[11], "./imgs/葡萄.png");
	loadimage(&img[12], "./imgs/芒果.png");
	loadimage(&img[13], "./imgs/菠萝.png");
	loadimage(&img[14], "./imgs/猕猴桃.png");
	loadimage(&img[21], "./imgs/竖线.png");
	loadimage(&img[22], "./imgs/横线.png");
	loadimage(&img[23], "./imgs/下右.png");
	loadimage(&img[24], "./imgs/下左.png");
	loadimage(&img[25], "./imgs/上右.png");
	loadimage(&img[26], "./imgs/上左.png");
	loadimage(&img[27], "./noChangeSizeImgs/暂停.png");
	//关卡配置
	all_time = eve_start_time[gk];
	remind_price = eve_remind_price[gk];
	add_time=eve_add_time[gk];
	cag = eve_cag[gk];
	all_num = eve_num[gk];
}//初始化
```



为了匹配关卡参数，对生成函数也做了修改，原来考虑为了避免没有可以消除的水果，所以让每一组生成的水果都可以消除，然后最终生成数字矩阵，但是在测试之后这样反而导致了水果扎堆，而且最终的数量也不好管理，所以干脆移除必须要可以消除这个条件，直接找空位，见缝插针，生成指定组数，最终试验的效果也还不错。

```cpp
/*生成矩阵*/
void create_mat(int max_num) {//采用随机生成方式,生成max_num组
	srand((unsigned)time(NULL));
	bool t[5000];//标记数组,标记有没有数字
	for (int i = 0; i <= (N-2) * (N-2) + 1; i++)//矩阵一维化
		t[i] = 0;
	for (int i = 0; ; i++) {
		for (int now = 1; now <= cag; now++) {
			int z1, z2;//找到两个没出现过的位置
			while (1) {
				z1 = rand() % ((N-2)*(N-2)) + 1;//取模和乘的优先级一样...
				if (!t[z1]) {
					t[z1] = 1;
					break;
				}
			}
			while (1) {
				z2 = rand() % ((N-2)*(N-2)) + 1;
				if (!t[z2]) {
					t[z2] = 1;
					break;
				}
			}
			int x1 = (z1 - 1) % (N - 2) + 1;
			int y1 = (z1 - 1) / (N - 2) + 1;
			int x2 = (z2 - 1) % (N - 2) + 1;
			int y2 = (z2 - 1) / (N - 2) + 1;//计算行列
			mat[y1][x1] = now;
			mat[y2][x2] = now;//全部随机生成
			if (i * cag + now == max_num) {
				show_mat(mat);
				return;
			}
		}
	}
}//生成矩阵
```



## 关卡切换

有了每一关的难度参数之后，如何连接呢，一个for循环就可以搞定，所以将游戏部分的主控程序做一个小修改，while(1)是为了可在一局结束之后，可以再开而不是直接中断程序。

```cpp
/*总控程序*/
void game() {
	initgraph(MAXX + 401, MAXY + 1);
	while (1) {
		for (now_level = 0; now_level <= 6; now_level++) {//七关
			printNextLevel();//打印转场动画
			all_ini(now_level);//初始化所有矩阵和图片库
			create_mat(all_num);//生成矩阵
			int check = play();//开始游戏并返回游戏状态
			if (!check) {
				break;//游戏结束
			}
			else if (now_level == 6) {
				prinfAllOk();//打印全部通关
			}
		}
	}
	//closegraph();//关闭窗口
}//总控程序
```



## 关卡动画

新增的需要打印的主要有全通关和关卡切换的界面，比较简单，就不展开了，具体直接看代码。

**到这里就全部结束了，以下是全部源码,项目结构在上一篇说明了**

**head.h**

```cpp
#define _CRT_SECURE_NO_WARNINGS 1
#include<stdio.h>
#include<algorithm>
#include<stack>
#include <stdlib.h>
#include <time.h>
#include<graphics.h>//图形绘制库
#include<string>
#include<iostream>
using namespace std;

const int N = 12 + 2; // 游戏界面加上隐藏一圈的大小,第一个数字是真正的游戏界面大小
const int inf = 0x3f3f;
#define SIZE 60 //图片尺寸
#define MAXX N*SIZE
#define MAXY N*SIZE
#define RIGHT 77
#define LEFT 75
#define UP 72
#define DOWN 80
#define ENTER 13
#define W 119
#define A 97
#define S 115
#define D 100
#define KONG 32
#define ESC 27//获取键值
#define EASY 130
#define NORMAL 90
#define DIFFICULT 70
#define DET 3 //连击间隔

void game();//游戏函数入口
```



**main.cpp**

```cpp
#include"head.h"

int main() {
	game();
	return 0;
}
```



**tool.cpp**

```cpp
#include"head.h"
#include"conio.h"

/*全局变量*/
struct node {
	int x, y, dir;//横坐标,纵坐标,方向(上下或者左右)
};

int dirx[] = { 0,1,0,-1 };
int diry[] = { -1,0,1,0 };
stack<node>st;//记录路径
IMAGE img[40];//存储图片
int time0;//游戏开始时间
int rem_num;//消除个数
int flag[N][N];//次数,计算能否相连要用
int mat[N][N];//游戏界面矩阵,记录了每个位置的图片编号
int tmp[N][N];//暂存矩阵，标记线
//显示模块控制模块
clock_t start, stop;//clock_t为clock()函数返回的变量类型
double duration;//记录被测函数运行时间，以秒为单位
bool if_time;//标记是否计时
int max_num;//最高连击次数
int now_num;//当前连击次数
int now_level;//当前关卡
int k = (N - 2) / 2;//大小参数，为了调节不同大小适应的方阵距离
int kr=SIZE/5;//表示当前大小的方块对应的圆半径系数
//关卡参数
int all_time;//可用初始时间
int add_time;//消除一个加1秒
int remind_price;//提示一次需要一秒代价
int all_num;//水果对数目
int cag;//水果种类，1-7
//下面是关卡参数配置
int eve_num[] = {1, 30, 35, 40, 45, 50,60 };//每一关的水果组数
int eve_cag[] = { 1, 8, 10, 12, 13, 14 ,14};//每一关的种类
int eve_start_time[] = { 100,15,15,20,20,30,30 };//每一关的初始时间
int eve_add_time[] = { 100,2,2,1,1,1 ,1};//每一关的消除加成时间
int eve_remind_price[] = { 1, 1, 1, 1, 2, 2 ,2 };//提示代价

/*初始化*/
void all_ini(int gk) {//传入关卡值
	for (int i = 0; i < N; i++) {
		for (int j = 0; j < N; j++) {
			mat[i][j] = 0;
		}
	}
	//默认配置
	all_num = (N - 2)*(N - 2)/2;//最多生成的组数
	if_time = 0;//一开始的不需要计时
	time0 = time(0);//开始时间
	rem_num = 0;//消除数量
	max_num = 1;//最大连击
	now_num = 1;//当前连击
	loadimage(&img[1], "./imgs/西瓜.png");
	loadimage(&img[2], "./imgs/苹果.png");
	loadimage(&img[3], "./imgs/草莓.png");
	loadimage(&img[4], "./imgs/梨.png");
	loadimage(&img[5], "./imgs/火龙果.png");
	loadimage(&img[6], "./imgs/桃子.png");
	loadimage(&img[7], "./imgs/香蕉.png");
	loadimage(&img[8], "./imgs/橙子.png");
	loadimage(&img[9], "./imgs/橘子.png");
	loadimage(&img[10], "./imgs/荔枝.png");
	loadimage(&img[11], "./imgs/葡萄.png");
	loadimage(&img[12], "./imgs/芒果.png");
	loadimage(&img[13], "./imgs/菠萝.png");
	loadimage(&img[14], "./imgs/猕猴桃.png");
	loadimage(&img[21], "./imgs/竖线.png");
	loadimage(&img[22], "./imgs/横线.png");
	loadimage(&img[23], "./imgs/下右.png");
	loadimage(&img[24], "./imgs/下左.png");
	loadimage(&img[25], "./imgs/上右.png");
	loadimage(&img[26], "./imgs/上左.png");
	loadimage(&img[27], "./noChangeSizeImgs/暂停.png");
	//关卡配置
	all_time = eve_start_time[gk];
	remind_price = eve_remind_price[gk];
	add_time=eve_add_time[gk];
	cag = eve_cag[gk];
	all_num = eve_num[gk];
}//初始化

/*初始化flag*/
void ini_flag(int x, int y,int ini_num) {//相当于x-行,y-列,最后一个是初始化的值
	for (int i = 0; i < N; i++)
		for (int j = 0; j < N; j++) {
			flag[i][j] = ini_num;
		}
	flag[x][y] = 0;
}//初始化flag

/*清除圆并补齐画面*/
void clearRound(int x,int y,int r,int detx,int dety) {//分别是xy点坐标，半径，偏移量
	int real_x, real_y;
	real_x = x * SIZE + SIZE / 2+detx;
	real_y = y * SIZE + SIZE / 2+dety;
	clearcircle(real_x, real_y, r);
	if(mat[y][x])
		putimage(x*SIZE, y*SIZE, &img[mat[y][x]]);//补上图片
}//清除圆并补齐画面

/*画一个圆*/
void drawRound(int x,int y,int r,int detx,int dety,int color){
	int real_x = x * SIZE + SIZE / 2;//圆心坐标
	int real_y = y * SIZE + SIZE / 2;
	circle(real_x + detx, real_y + dety, r);
	setfillcolor(color);  //填充色为蓝色
	fillcircle(real_x+detx, real_y +dety, r);
}//画一个圆

/*转换数字为字符串*/
void deal(int x, char *a) {
	stack <int>sst;
	while (x) {
		sst.push(x % 10);
		x /= 10;
	}
	int l = 0;
	while (!sst.empty()) {
		a[l++] = sst.top() + ''0'';
		sst.pop();
	}
	a[l] = ''\0'';
	if (a[0] == ''\0'') {
		a[0] = ''0'';
		a[1] = ''\0'';
	}
}//转换数字为字符串

/*计算和统计模块*/
void printCal() {
	char a[100];

	//计算连击
	settextcolor(WHITE);
	settextstyle(40, 40, 0);
	outtextxy(N*SIZE+40, 10, "当前关卡");
	settextstyle(100, 100, 0);
	deal(now_level, a);
	outtextxy(N*SIZE+170, 100, a);

	//统计数目
	settextcolor(WHITE);
	settextstyle(30, 30, 0);
	outtextxy(N*SIZE+40, N*SIZE-250, "剩余水果对");
	settextstyle(100, 100, 0);
	deal(all_num, a);
	outtextxy(N*SIZE + 150, N*SIZE - 170, a);
}//计算连击和统计模块

/*打印时间耗尽*/
void print_timeOver() {
	//打印时间耗尽
	settextcolor(BROWN);
	int size = k * 150 / (N - 2);//字体大小
	settextstyle(size, size, 0);
	outtextxy(N*SIZE / 2 - 4 * size, N*SIZE / 2 - 2 * size, "时间耗尽");
	system("pause");
}//打印时间耗尽

/*打印时间*/
void printTime() {
	char a[100];
	//剩余时间
	settextcolor(WHITE);
	settextstyle(40, 40, 0);
	outtextxy(N*SIZE + 40, 250, "剩余时间");
	settextstyle(100, 100, 0);
	int t = all_time + (rem_num*add_time) - (time(0) - time0);
	deal(t, a);
	if (t % 10 == 9) {
		clearrectangle(N*SIZE + 170, 350, N*SIZE + 400, 450);
	}
	outtextxy(N*SIZE + 170, 350, a);
}//打印时间

/*结束*/
void printOver() {
	//打印消除完毕
	settextcolor(BROWN);
	int size = k*150 / (N - 2);//字体大小
	settextstyle(size, size, 0);
	outtextxy(N*SIZE/2-4*size, N*SIZE / 2 - 2*size, "全部消除");
	line(size, N*SIZE / 2 - size/2, N*SIZE - size, N*SIZE / 2 -  size/2);
	//打印最高连击
	char a[100];
	settextcolor(WHITE);
	size = k*90 / (N - 2);
	settextstyle(size, size, 0);
	outtextxy(N*SIZE / 2 -6 * size, N*SIZE / 2 , "最高连击次数");
	size = k*150 / (N - 2);
	settextstyle(size, size, 0);
	deal(max_num, a);
	outtextxy(N*SIZE / 2 -size/2, N*SIZE / 2+size, a);
	//打印时间
	printTime();
	//打印消除完毕
	settextcolor(WHITE);
	size = k * 60 / (N - 2);//字体大小
	settextstyle(size, size, 0);
	outtextxy(N*SIZE / 2 - 7 * size, N*SIZE / 2 + 6 * size, "点击任意键继续...");
	system("pause");
}//结束

/*打印关卡过渡动画*/
void printNextLevel() {
	cleardevice();
	char a[100];
	char info[100] = "第";//拼接数组
	//打印时间耗尽
	settextcolor(BROWN);
	int size = k * 150 / (N - 2);//字体大小
	settextstyle(size, size, 0);
	deal(now_level, a);
	strcat(info, a);
	strcat(info, "关");
	outtextxy(MAXX / 2+200-size*2, MAXY / 2-size, info);
	Sleep(1000);
	cleardevice();
}//打印关卡过渡动画

/*打印全部通关*/
void prinfAllOk() {
	settextcolor(WHITE);
	int size = k * 150 / (N - 2);//字体大小
	settextstyle(size, size, 0);
	cleardevice();
	outtextxy(N*SIZE / 2 - 4 * size, N*SIZE / 2 - 2 * size, "恭喜你全部通关");
	system("pause");
}//打印全部通关

/*打印当前状态*/
void show_mat(int Mat[N][N]) {
	printCal();
	line(N*SIZE, 0, N*SIZE, MAXY);
	for (int i = 0; i <= N - 1; i++) {
		for (int j = 0; j <= N - 1; j++) {
			if (Mat[i][j] < 0) {
				putimage(j*SIZE, i*SIZE, &img[-Mat[i][j]+20]);
			}
			else if (Mat[i][j] == 0)
				continue;
			else
				putimage(j*SIZE, i*SIZE, &img[Mat[i][j]]);
		}
	}
}//打印当前状态

/*搜索答案*/
void dfs(int x, int y, int qdx, int qdy, int zdx, int zdy, int step, int now, int &stats) {
	if (stats)return;//找到了就不找了
	//判断,参数分别是x,y,(x向右,y向下),起点xy和终点xy,当前转弯次数,当前方向参数，0123分别是上右下左,当前寻找的状态
	if (step == 3) {//超出步数
		if (!st.empty()) {//拿出去
			st.pop();
		}
		return;
	}
	if (zdx == x && zdy == y && step <= 2) {//终点
		stats = 1;
		return;
	}
	for (int i = now; i < now + 4; i++) {//从当前方向开始找
		int index = i % 4;
		int xx = x + dirx[index];
		int yy = y + diry[index];
		if (i == now + 2 && (x != qdx || y != qdy) || xx < 0 || yy < 0 || xx == N || yy == N || // 第一个的逻辑是不能往回走，除非是起点
			mat[yy][xx] && !(xx == zdx && yy == zdy))//不合法,最后一个代表已经被占据并且不是终点
			continue;
		if ((x != qdx || y != qdy) && (i % 2 == 0 && (zdx - qdx)*(xx - x) < 0 || i % 2 == 1 && (zdy - qdy)*(yy - y) > 0))//竖着走和横着走不可能偏离终点
			continue;
		if (now == index || x == qdx && y == qdy) {//方向相同或者是起点
			st.push({ xx,yy,i % 2 });
			dfs(xx, yy, qdx, qdy, zdx, zdy, step, index, stats);
		}
		else {
			st.push({ xx,yy,i % 2 });
			dfs(xx, yy, qdx, qdy, zdx, zdy, step + 1, index, stats);
		}
		if (stats)return;
	}
	if (stats)return;
	if (!st.empty()) {//四个方向都没有
		st.pop();
	}
	return;
}//搜索答案

/*生成矩阵*/
void create_mat(int max_num) {//采用随机生成方式,生成max_num组
	srand((unsigned)time(NULL));
	bool t[5000];//标记数组,标记有没有数字
	for (int i = 0; i <= (N-2) * (N-2) + 1; i++)//矩阵一维化
		t[i] = 0;
	for (int i = 0; ; i++) {
		for (int now = 1; now <= cag; now++) {
			int z1, z2;//找到两个没出现过的位置
			while (1) {
				z1 = rand() % ((N-2)*(N-2)) + 1;//取模和乘的优先级一样...
				if (!t[z1]) {
					t[z1] = 1;
					break;
				}
			}
			while (1) {
				z2 = rand() % ((N-2)*(N-2)) + 1;
				if (!t[z2]) {
					t[z2] = 1;
					break;
				}
			}
			int x1 = (z1 - 1) % (N - 2) + 1;
			int y1 = (z1 - 1) / (N - 2) + 1;
			int x2 = (z2 - 1) % (N - 2) + 1;
			int y2 = (z2 - 1) / (N - 2) + 1;//计算行列
			mat[y1][x1] = now;
			mat[y2][x2] = now;//全部随机生成
			if (i * cag + now == max_num) {
				show_mat(mat);
				return;
			}
		}
	}
}//生成矩阵

/*标记线*/
void deal(int x1,int x2,int y1,int y2) {
	for (int i = 0; i < N; i++)
		for (int j = 0; j < N; j++)
			tmp[i][j] = mat[i][j];//复制矩阵
	int bef_dir = st.top().dir;//初始方向
	int bef_x = st.top().x;
	int bef_y = st.top().y;
	st.pop();//终点不需要变成线
	while (!st.empty()) {
		int xx = st.top().x;
		int yy = st.top().y;
		int dir = st.top().dir;
		st.pop();
		if (dir == 0) {//上下
			//tmp[st.top().y][st.top().x] = -1;//表示上下
			if (bef_dir==dir) {
				//跟上一个一样
				tmp[yy][xx] = -1;//上下
			}
			else {         //左右
				if (!st.empty() && st.top().y < yy || st.empty() && y1 < yy) {
					//跟下一个比定上下,此时是下一个在上面
					if (xx < bef_x)//向左走
						tmp[yy][xx] = -5;//上右
					else
						tmp[yy][xx] = -6;//上左
				}
				else {
					if (xx < bef_x)//向左走
						tmp[yy][xx] = -3;//下右
					else
						tmp[yy][xx] = -4;//下左
				}
			}
		}
		else {
			//tmp[st.top().y][st.top().x] = -2;//表示左右
			if (bef_dir == dir) {//也是左右
				tmp[yy][xx] = -2;
			}
			// x1 y1 是起点坐标
			else {
				if (!st.empty() && st.top().x < xx || st.empty() && x1 < xx) {//下一个在左边
					if (yy < bef_y) //向上走
						tmp[yy][xx] = -4;//下左
					else
						tmp[yy][xx] = -6;//上左
				}
				else {
					if (yy < bef_y)//向上走
						tmp[yy][xx] = -3;//下右
					else
						tmp[yy][xx] = -5;//上右
				}
			}
		}
		bef_x = xx;
		bef_y = yy;
		bef_dir = dir;//更新表示上一个状态的量
	}
}//标记线

/*处理消除之后的样子以及计时模块*/
bool deal_after(int x1,int x2,int y1,int y2) {
	//计时模块
	rem_num++;
	all_num--;
	if (if_time) {
		stop = clock();//停止计时
		duration = (double)(stop - start) / CLOCKS_PER_SEC;//计算运行时间
		if (duration <= DET) {
			now_num += 1;
			max_num = max(max_num, now_num);
		}
		else {
			now_num = 1;//重置连击次数
		}
	}
	//下面显示出消除的样子
	show_mat(tmp);
	Sleep(300);//程序暂停
	mat[y1][x1] = 0;
	mat[y2][x2] = 0;
	cleardevice();
	show_mat(mat);
	if (!all_num) {
		return 1;//代表可以结束游戏了
	}
	start = clock();//开始计时
	if_time = 1;//计时标记
	return 0;
}

/*提示模块*/
bool remind(int &x1, int &y1, int &x2, int &y2) {//返回是否可以消除,返回值内包括起点终点坐标
	for (int i1 = 1; i1 < N - 1; i1++) {
		for (int j1 = 1; j1 < N - 1; j1++) {
			if (!mat[i1][j1])
				continue;//空格
			for (int i2 = 1; i2 < N - 1; i2++) {
				for (int j2 = 1; j2 < N - 1; j2++) {
					if (!mat[i2][j2] || i1 == i2 && j1 == j2||mat[i2][j2]!=mat[i1][j1])
						continue;
					int check = 0;
					dfs(j1, i1, j1, i1, j2, i2, 0, 0, check);
					if(check) {
						x1 = j1;
						y1 = i1;
						x2 = j2;
						y2 = i2;
						return 1;
					}
					else {
						while (!st.empty())//否则要清空栈
							st.pop();
					}
				}
			}
		}
	}
	return 0;
}//提示模块

/*刷新模块*/
void refresh() {
	int x, y;
	srand((unsigned)time(NULL));
	ini_flag(0, 0, 0);//初始化flag，借用flag数组标记
	for (int i = 1; i <= N - 1; i++) {
		for (int j = 1; j <= N - 1; j++) {
			if (mat[i][j]&&!flag[i][j]) {//如果里面是有数字的并且不是前面的移过来的
				while (1) {
					x = rand() % (N - 2) + 1;
					y = rand() % (N - 2) + 1;//找到两个没出现过的位置
					if (!mat[y][x]) {//空的
						mat[y][x] = mat[i][j];
						mat[i][j] = 0;
						flag[y][x] = 1;//标记点
						break;
					}
				}
			}
		}
	}
	cleardevice();
	show_mat(mat);
}//刷新模块

/*游戏暂停*/
int stop_game() {
	int stop_time = time(0);
	cleardevice();
	putimage(MAXX / 2 + 100, MAXY / 2 - 100, &img[27]);
	system("pause");//停止
	return time(0)-stop_time;//返回暂停时间
}//游戏暂停

/*游戏操作部分*/
bool play() {
	int x1=1, y1=1, x2=1, y2=1,now=1;//记录当前位置(x2,y2)和上一个位置(x1,y1),now记录当前状态，1是第一个，2是第二个
	int bef_t = time(0)-time0;
	while (all_num) {//只要还剩下
		MOUSEMSG m;
		if (bef_t != time(0) - time0) {
			printTime();
		}
		printTime();//打印时间
		if (all_time + (rem_num*add_time) - (time(0) - time0)==0) {//时间耗尽
			print_timeOver();
			return 0;//游戏结束
		}
		if (MouseHit())//是否有鼠标消息
		{
			m = GetMouseMsg();
			if (m.uMsg == WM_LBUTTONDOWN)//左键
			{
				//rectangle(m.x - 5, m.y - 5, m.x + 5, m.y + 5);
				if (m.x > N*SIZE || m.y > N*SIZE)
					continue;
				m.x = m.x / SIZE;
				m.y = m.y / SIZE;
				if (now == 1) {//第一个确定状态只要不越界都可以,然后记录上一个位置
					x1 = m.x;
					y1 = m.y;//x1，y1存储上一次状态
					if (!mat[y1][x1])
						continue;//空格无效
					drawRound(x1, y1, kr, 0, 0, GREEN);
					now = 2;
				}
				else {//第二个要判定状态
					x2 = m.x;
					y2 = m.y;
					if (mat[y1][x1] != mat[y2][x2] || x1 == x2 && y1 == y2) {
						clearRound(x1, y1, kr, 0, 0);//重新选择
						now = 1;
						continue;
					}
					if (x1 > 0 && x1 <= N - 2 && y1 > 0 && y1 <= N - 2 && x2 > 0
						&& x2 <= N - 2 && y2 > 0 && y2 <= N - 2) {
						int check = 0;
						dfs(x1, y1, x1, y1, x2, y2, 0, 0, check);
						if (check) {
							deal(x1, x2, y1, y2);//标记线
							bool if_over = deal_after(x1, x2, y1, y2);//判断是否结束，同时善后工作
							if (if_over) {
								printOver();//打印结束语句
								return 1;//通过当前关卡
							}
							int check = remind(x1, y1, x2, y2);
							if (!check) {
								refresh();//没有可以消除的了,刷新
							}
							else {
								while (!st.empty())
									st.pop();
							}
						}
						else {
							clearRound(x1, y1, kr, 0, 0);//不能消除
							while (!st.empty())//清空栈
								st.pop();
						}
					}
					now = 1;
				}
			}
		}
		if (_kbhit()) {//如果有按键
			int ch = _getch();
			if (ch == KONG) {//提示按钮
				int check = remind(x1,y1,x2,y2);
				if (check) {
					deal(x1, x2, y1, y2);//标记线
					show_mat(tmp);
					Sleep(remind_price*1000);//代价
					cleardevice();
					show_mat(mat);
				}
			}
			else if (ch == ESC) {
				time0+=stop_game();//要把计时起点向后挪动
				cleardevice();
				show_mat(mat);
			}
			else if (ch == RIGHT) {//刷新
				refresh();
			}
		}
	}
}//游戏操作部分

/*总控程序*/
void game() {
	initgraph(MAXX + 401, MAXY + 1);
	while (1) {
		for (now_level = 5; now_level <= 6; now_level++) {//七关
			printNextLevel();//打印转场动画
			all_ini(now_level);//初始化所有矩阵和图片库
			create_mat(all_num);//生成矩阵
			show_mat(mat);
			int check = play();//开始游戏并返回游戏状态
			if (!check) {
				break;//游戏结束
			}
			else if (now_level == 6) {
				prinfAllOk();//打印全部通关
			}
		}
	}
	//closegraph();//关闭窗口
}//总控程序
```



# **效果图**

![img](https://s21.ax1x.com/2025/11/24/pZAA9Hg.png)

![img](https://s21.ax1x.com/2025/11/24/pZAAPEQ.png)',0,'2022-09-09 19:12:32','2022-09-09 19:12:32');

INSERT INTO article_detail(article_id,version,content,deleted,create_time,update_time) VALUES(3,1,_utf8mb4' 刚开始写的时候想想这个应该是非常好写的，但是写到后面，尤其是遇到很多莫名其妙的bug之后，发现似乎没那么简单。以下是开发过程中的一些想法，在这里做个笔记。

> **目录**
>
> [游戏介绍](#游戏介绍)
>
> [素材引入](#素材引入)
>
> [初始化](#初始化)
>
> [全局初始化](#全局初始化)
>
> [关卡初始化](#关卡初始化)
>
> [初始化效果](#初始化效果)
>
> [对于坦克的操作 ](#对于坦克的操作 )
>
> [坦克队列初始化](#坦克队列初始化)
>
> [自身坦克操作](#自身坦克操作 )
>
> [敌方坦克的操作](#敌方坦克的操作)
>
> [对于子弹的操作](#对于子弹的操作 )
>
> [子弹生成](#子弹生成)
>
> [子弹出界](#子弹出界)
>
> [子弹经过空地](#子弹经过空地)
>
> [子弹打中家](#子弹打中家)
>
> [子弹打中坦克 ](#子弹打中坦克)
>
> [击中障碍物](#击中障碍物)
>
> [对于道具的操作 ](#对于道具的操作)
>
> [道具的生成](#道具的生成)
>
> [炸弹](#炸弹)
>
> [冰冻](#冰冻)
>
> [修复](#修复)
>
> [升级 ](#升级 )
>
> [最终效果 ](#最终效果)
>
> [总结](#总结)
>
> [完整代码](#完整代码)

# 游戏介绍

坦克大战大家都很熟悉，就是守护自己的老家不要被炸掉，同时也要保证自身死亡次数在一定范围之内，消灭所有敌人。

下面是一个gif:

[https://ts1.cn.mm.bing.net/th/id/R-C.4015145422ab96da92232f86813541e4?rik=I7AWR37N6ZQIoQ&riu=http%3a%2f%2fimg0.xinmin.cn%2f2020%2f11%2f13%2f20201113153532276658.gif&ehk=w9TeEKSyUUjt5Y68Xw%2fc9GRNcjGX6GDDn5t%2bUC2Usf0%3d&risl=&pid=ImgRaw&r=0](https://ts1.cn.mm.bing.net/th/id/R-C.4015145422ab96da92232f86813541e4?rik=I7AWR37N6ZQIoQ&riu=http%3A%2F%2Fimg0.xinmin.cn%2F2020%2F11%2F13%2F20201113153532276658.gif&ehk=w9TeEKSyUUjt5Y68Xw%2Fc9GRNcjGX6GDDn5t%2BUC2Usf0%3D&risl=&pid=ImgRaw&r=0)

# 素材引入

游戏的图片素材分为四类，一类是场景，一类是坦克，一类是道具，还有其他类，包括家的图，爆炸，子弹等图案，整理之后我决定用ppt制作坦克，然后找了一些网上的图片，整合之后如下。

至于素材应该放在哪里，可以参考我前面写的连连看开发的博客[伪C++开发小游戏---连连看_czc131的博客-CSDN博客_c++制作连连看struct](https://blog.csdn.net/weixin_60360239/article/details/126737392?spm=1001.2014.3001.5502)

![img](https://s21.ax1x.com/2025/11/25/pZAeMsx.png)

# 初始化

 走完基本流程，我们应该可以利用initgraph()函数生成一个空的画布，那么首先就要进行初始化工作，为了可扩展性，我把初始化分为了两种，一种是游戏初始化，一种是关卡初始化（虽然我自己就做了一关），游戏初始化主要针对全局初始化，尤其每一关都是一样的，包括导入图片，对不同类型坦克的参数设置，关卡初始化就是对生命值等初始化，包括生成地图，标记初始化。

## 全局初始化

全局初始化主要针对一些乱七八糟的东西，一些定义在最外面的全局变量就不说了，其实最好还是放在里面，在这里我定义了type结构体数组，来存放每种类型坦克对应的一些属性，同时开辟了tank结构体数组，存放当前在场活动的所有坦克的相关信息， 而ZIdan结构体队列就存放当前活动的所有子弹信息。

具体就不放出，就在**完整代码**的最前面。

## 关卡初始化

```cpp
/*关卡初始化*/
void ini_every(int mp[][5], int num) {
	rest_num = 20;//敌方坦克数量
	my_lives = 3;
	tool.exist = 0;
	level = 1;//一级
	putimage(MAXX + 100, 100, &img[1]);
	putimage(MAXX + 100, 200, &img[4]);
	PrintTankNum();
	srand((unsigned)time(NULL));//调整种子
	rand_seed = rand();
	line(MAXX + 1, 0, MAXX + 1, MAXY + 1);//分割线
	putimage(7 * SIZE * 4, 12 * SIZE * 4, &img[14]);
	for (int i = 0; i <= NUMY; i++){
		for (int j = 0; j <= NUMX; j++) {
			mat[i][j] = 0;
			flag[i][j]= -1;//先初始化为-1
			tool.flag[i][j] = 0;
		}
	}
	for (int i = 12 * 4; i <= 12 * 4 + 8; i++) {
		for (int j = 7 * 4; j <= 7 * 4 + 8; j++) {
			flag[i][j] = 10;//标记为老家位置
		}
	}
	for (int i = 0; i < num; i++) {
		int k = mp[i][0];
		int x1 = mp[i][1]*4;
		int y1 = mp[i][2]*4;
		int x2 = mp[i][3]*4-1;
		int y2 = mp[i][4]*4-1;//右下角方块的左上角坐标
		for (int xx = x1; xx <= x2; xx++) {
			for (int yy = y1; yy <= y2; yy++) {
				mat[yy][xx] = k;
			}
		}
		if (k == 2) {//泥土块最小单位SIZE
			for (int xx = x1; xx <= x2; xx++) {
				for (int yy = y1; yy <= y2; yy++) {
					putimage(xx*SIZE, yy*SIZE, &img[k + 10]);
				}
			}
		}
		else {//铁块和草方块最小单位2*SIZE
			for (int xx = mp[i][1]*2; xx <= mp[i][3]*2-1; xx++) {
				for (int yy = mp[i][2]*2; yy <= mp[i][4]*2-1; yy++) {
					putimage(xx*SIZE*2 ,yy*SIZE*2, &img[k + 10]);
				}
			}
		}
	}
}//关卡初始化
```



我自己是把**15个像素作为一个最小单位的长度SIZE**，后面的移动和方块的消除也是基于最小单位长度来更新，基于消除的特殊性，一次泥土方块的消除是SIZE，因此最小的单位是SIZE,而铁块和的消除基本是以2*SIZE,所以最小单位也就是2*SIZE，至于怎么给出这张地图，**只要给定每个区块的类型和左上角右下角坐标就可以确定一处障碍物**，所以我就自己以4*SIZE为最小单位画了图，然后一个个给出，数组如下，每个大括号是一处，第一个元素是类型。还有几个二维数组，是用来标记的，都是以SIZE为单位的下标，flag[][]标记坦克位置，mat[][]标记障碍物位置，tool结构体内存储道具信息，tool.exist()代表厂商是否有道具，tool.flag[][]存储道具在地图上的标记。同时**在程序中因为需要一直读取用户的操作，所以我就把10MS作为了更新的最小周期**，这在后面就是时间的最小单位，是来定义移速和攻速的。

```cpp
// 第一关地图，1表示铁块，2表示红砖，3表示草地
//后面是x1 y1 x2 y2 是在最小单元为2*SIZE的地图上的每个类型方块的对角线坐标
int map_1[][5] = { {2,0,2,2,8} ,{2,4,2,6,3},{1,7,0,9,2},{1,12,2,14,4},
				  {3,4,4,6,6},{2,6,4,14,6},{2,14,4,16,6},{1,2,6,4,10},
				  {2,1,10,3,11},{3,4,7,5,8},{1,5,7,7,9},{2,7,7,11,9},
				  {1,11,7,13,9},{2,13,7,15,14},{2,1,11,3,14},{1,4,3,6,4},
				  {2,6,12,7,14},{2,6,11,10,12},{2,9,12,10,14} };
```



##  初始化效果

![img](https://s21.ax1x.com/2025/11/25/pZAeQL6.png)

> 下面从对于坦克的操作，对于子弹的操作和对于道具的操作来展开。

# 对于坦克的操作

首先是坦克，坦克分为自己的和对方的，因为游戏本身是个单机游戏，所以不会有什么其他人来操作，所以还是得要自己来控制敌方坦克，本质还是用随机数来控制，当然也要让敌方坦克更聪明一点才行。

## 坦克队列初始化

先把自己坦克和对方坦克生成好。这里我用了几个函数，首先自身的出生点就是家的左边，对方的出生点我设置在了最上面四个，一开始我就没有用随机数选定生成位置，就直接生成在了前三个，为了提高程序复用性，我就把生成一个坦克单独拉出来写了一段，然后生成多个无非就是不断调用函数。**其中标记随机种子链的是因为程序执行速度很快，用随机时间种子最后出来的几个是一样的，所以我就用了生成的随机数做种子，最后出来的效果还可以。**

```cpp
/*产生一个坦克*/
Tank add_tank(int add_place,int wz) {
	//add_place表示产生位置，取值1234表示敌方坦克的出生位置，wz表示在坦克数组中的位置
	Tank t;
	t.dir = 3;
	t.bef_fight = 0;
	t.bef_move = 0;
	t.stop_time = rand()%5;
	t.dir_stop_time = 0;
	if (add_place) {//敌方坦克
		srand(rand_seed);//重置时间种子
		rand_seed = rand();//随机种子链
		int index = rand() % all_tank_num + 4;
		t.move_type = rand() % 2 + 1;
		t.type = index;
		t.HP = type[index].HP;
		t.x = birth_wz[add_place][0];
		t.y = birth_wz[add_place][1];
		t.state = 1;//表示活着
		mark_tank(t.x, t.y, wz);
	}
	else {//自己
		t.HP = type[level].HP;
		t.type = level;//通过type索引可以得到速度等信息
		t.x = birth_wz[0][0];
		t.y = birth_wz[0][1];
		mark_tank(t.x, t.y, 0);//0号标记是自己
	}
	return t;
}//产生一个坦克

/*初始化坦克队列*/
void ini_tank() {
	num_tank = 0;
	tank[0]=add_tank(0,0);//0表示产生己方坦克，其他表示其他坦克，编号index
	//在1 2 3号出生点设置敌方坦克
	for (int i = 1; i <= 3; i++) {
		num_tank++;
		tank[i]=add_tank(i, num_tank);
	}
}//初始化坦克队列
```



## 自身坦克操作

 坦克自身的操作就是移动和发射子弹，移动需要判定位置是否合法，发射子弹也需要判定子弹的合法性，主要就是调用函数，当然函数也要写的，具体见完整项目。（感觉写了很多不必要的）

下面是针对自身的主控函数部分：

```cpp
	int num_t = 0;
	while (1) {
		if (KEY_DOWN(VK_UP)) {
			control_dir(0);
		}
		if (KEY_DOWN(VK_RIGHT)) {
			control_dir(1);
		}
		if (KEY_DOWN(VK_DOWN)) {
			control_dir(2);
		}
		if (KEY_DOWN(VK_LEFT)) {
			control_dir(3);
		}
		if (KEY_DOWN(VK_SPACE)) {
			if (tank[0].bef_fight >= type[tank[0].type].Fight_det) {//至少要过几个时间段才可以发射子弹
				tank[0].bef_fight = 0;
				add_zidan(tank[0]);//添加一个子弹
			}
		}
		if (KEY_DOWN(VK_ESCAPE)) {
			stop();
		}
		if (stop_epoch) {
			stop_epoch--;
		}
		else {
			check_tank();//其他坦克
		}
		Sleep(10);//更新频率
		tank[0].bef_fight = (tank[0].bef_fight + 1) % 10000;//距离上次发射时间
		tank[0].bef_move = (tank[0].bef_move + 1) % 10000;//距离上次移动时间
	}
```



##  敌方坦克的操作

敌方坦克最大的任务其实就是打掉我们自己的家，在一开始我尝试了每个更新周期去随机调整坦克方向，就是非常盲目的乱动，最后出来的效果非常差，坦克像个陀螺一直在转，所以后来我就改进了一下坦克的路径更新算法，改成了每次**更新坦克在当前方向上移动的次数,**也就是确定了现在的方向是向下，那么接下来我给你一个范围内的随机数x，你接下来x次都向下走，并且为了避免坦克卡在障碍前面不动，我在坦克遇到障碍物时自动转向。为了真实一点模拟，我也让坦克有时候在遇到障碍物之后改变方向后不动，发射子弹，模拟破墙举动。最后最重要的就是坦克需要来打掉我们的家，所以必须要让坦克有向着家走的趋势，因此我**利用和x和y轴和家的对应xy轴距离占总距离   MAXX,MAXY的比例来作为概率，引导坦克向着中间下面走，或者向这边发射子弹。**简单来说就是坦克的y坐标距离家的y坐标越远，那么向下的概率也就越大。

至于射击，我用了概率方法，有50%概率发射子弹。

当然坦克的发射子弹和移动同样要受到更新周期的限制，并不是每个更新周期都能移动，而是隔几个之后才可以移动。

代码如下：

```cpp
/*控制其他坦克*/
void check_tank()
{
	for (int i = 1; i <= num_tank; i++) {//依次取出坦克
		if (!tank[i].state)continue;//已经没有了
		if (tank[i].bef_move >= type[tank[i].type].Speed) {
			//可能停留在原地的
			if (tank[i].stop_time) {
				tank[i].stop_time--;
				if (tank[i].bef_fight >= type[tank[i].type].Fight_det) {
					srand(rand_seed);//重置时间种子
					rand_seed = rand();//随机种子链
					int state = rand() % 100;
					if (state <= 50) {
						tank[i].bef_fight = 0;
						add_zidan(tank[i]);//添加一个子弹
					}
					else {
						tank[i].bef_fight = 0;
					}
				}
			}
			//表示要移动的
			else if (tank[i].dir_stop_time) {
				int xx = tank[i].x + dirx[tank[i].dir] * SIZE;
				int yy = tank[i].y + diry[tank[i].dir] * SIZE;//坐标
				if (check_wz(xx, yy, i))
				{
					draw_a_tank(tank[i], xx, yy);//xx,yy表示更新后的位置
					update_mark(tank[i].dir, xx, yy, i);//更新坦克位置信息
					tank[i].x = xx;
					tank[i].y = yy;
					tank[i].bef_move = 0;//更新时间
					tank[i].dir_stop_time--;
				}
				else
				{
					//先打一发
					if (tank[i].bef_fight >= type[tank[i].type].Fight_det) {
						tank[i].bef_fight = 0;
						add_zidan(tank[i]);//添加一个子弹
					}
					srand(rand_seed);//重置时间种子
					rand_seed = rand();//随机种子链
					int state = rand() % 100;
					tank[i].dir_stop_time = state % 10+1;//方向锁定次数
					if (state % 3==1) {
						tank[i].dir = (tank[i].dir + 1) % 4;
						if(state%2)
							tank[i].stop_time = state*2;//可能几个回合之内停在原地
					}
					else if(state % 3 == 2){
						tank[i].dir = (tank[i].dir - 1 + 4) % 4;
						if(state%2)
							tank[i].stop_time = state*2;//可能几个回合之内停在原地
					}
					else {
						tank[i].dir = (tank[i].dir + 2) % 4;//反向
					}
					draw_a_tank(tank[i], tank[i].x, tank[i].y);
				}
			}
			else {
				//换方向默认优先向着中间目标
				srand(rand_seed);//重置时间种子
				rand_seed = rand();//随机种子链
				int state = rand() % 100;
				//根据移动类型优化路径
				if (tank[i].move_type == 1) {
					if (abs(tank[i].x - MAXX / 2) * 2 / MAXX * 100 > state) {//距离中间远
						if (tank[i].x - MAXX / 2 >= 0) {
							tank[i].dir = 3;//左边
						}
						else {
							tank[i].dir = 1;//右边
						}
					}
					else if (abs(tank[i].y - MAXY) / MAXY * 100 > state) {//距离下面远
						tank[i].dir = 2;//向下走
					}
					else {
						tank[i].dir = state % 4;//随机换向
					}
				}
				else {
					if (abs(tank[i].y - MAXY) / MAXY * 100 > state) {//距离下面远
						tank[i].dir = 2;//向下走
					}
					else if (abs(tank[i].x - MAXX / 2) * 2 / MAXX * 100 > state) {//距离中间远
						if (tank[i].x - MAXX / 2 >= 0) {
							tank[i].dir = 3;//左边
						}
						else {
							tank[i].dir = 1;//右边
						}
					}
					else {
						tank[i].dir = state % 4;//随机换向
					}
				}
				tank[i].dir_stop_time = state % 10+1;//方向锁定次数
				draw_a_tank(tank[i], tank[i].x, tank[i].y);
			}
		}
		//控制射击
		if (tank[i].bef_fight >= type[tank[i].type].Fight_det) {
			srand(rand_seed);//重置时间种子
			rand_seed = rand();//随机种子链
			int state = rand() % 100;
			if (state <= 50) {
				tank[i].bef_fight = 0;
				add_zidan(tank[i]);//添加一个子弹
			}
			else {
				tank[i].bef_fight = 0;
			}
		}
		tank[i].bef_fight = (tank[i].bef_fight + 1) % 10000;//距离上次发射时间
		tank[i].bef_move = (tank[i].bef_move + 1) % 10000;//距离上次移动时间
	}
}//控制其他坦克
```



# 对于子弹的操作

> 子弹自我感觉是最难做的一部分，因为涉及到很多东西，尤其是子弹设计障碍物的部分，不知道写了多少废话代码，下面就几种情况展开。

## 子弹生成

 子弹的生成我想了两种方案，一种是生成在坦克外面，但是这样刚射出就要考虑是不是已经撞到障碍物，加上这段代码跟后面的兼容性并不好，所以我就考虑生成在坦克坐标之内，然后弹头坐标是当前坦克方向的中间线位置，所以需要做个标记count，如果第一次清除子弹那么就不需要把子弹标记这一块清除，防止把坦克也清除了。

```cpp
/*根据坦克产生一个子弹*/
void add_zidan(Tank t) {
	int tmp_type = t.type;//可以取到子弹的伤害
	Zidan d;
	d.dir = t.dir;
	int xx, yy;
	switch (t.dir) {//根据方向和坦克的左上角坐标确定子弹占据空间
	case 0:
		xx = t.x + 2 * SIZE;
		yy = t.y;
		break;
	case 1:
		xx = t.x + 4 * SIZE;
		yy = t.y + 2 * SIZE;
		break;
	case 2:
		xx = t.x + 2 * SIZE;
		yy = t.y + 4 * SIZE;
		break;
	case 3:
		xx = t.x;
		yy = t.y + 2 * SIZE;
		break;
	}
	d.x = xx;
	d.y = yy;
	d.who = t.type;//123都是自己的形态，代表是自己,否则是对方的
	d.count = 0;
	qz.push(d);
}//根据坦克产生一个子弹
```



接下去是对于子弹的更新，子弹实际上是存在一个队列中的（但实际上是当做数组在用），每一轮对没有更新过的子弹进行更新，对于无效的子弹就不再入队，否则更新后重新插入队列。

## 子弹出界

子弹出界直接就清除掉就好了。，也不用重新入队。

## 子弹经过空地

这种情况最简单，只要直接把子弹清空然后就可以，在子弹的下一个位置重新画一个，不要忘记

## 子弹打中家

这种就意味着游戏结束，直接返回一个值，然后标志游戏结束。

## 子弹打中坦克

 子弹打中坦克稍微麻烦一点，坦克的存储坐标是在左上角方块的左上角点，是一个4*4的点阵，表示了存放坦克的16个方块的左上角坐标，考虑到子弹从坦克的四周穿过不应该打中坦克，所以我就如下图的几个点标记为坦克的占据点，放在flag中，图中的方框就是坦克占据的空格，**flag中存放的是在坦克队列tank[]中的下标位置**，自己就是0，敌方坦克是一个大于0的数字，初始值是-1。

![img](https://s21.ax1x.com/2025/11/25/pZAeJFe.png)

 在判断时，就需要判断子弹的下一步位置是不是会击中如上位置，如果会那么就执行清空坦克的操作。

要注意的是，也需要查看子弹的性质，在我自己的设定中，敌方是不会互殴的，所以队友打到队友，只要把子弹清除就可以。

## 击中障碍物

可能是我想复杂了，这个模块是我写的最麻烦的，个人觉得，因为感觉要要考虑的实在太多，写了非常多的判断。每次击中方块可以消除四个并排单位大小的方块，其中的组合大致分为如下几类：打中铁块，打中草丛，打中泥土，还有打中其中几种的混合。由于一般的坦克打中铁块是不能消除的，所以遇到铁块和泥土这样的组合，你要保证消除是有效的，也就是消除泥土而不消除铁块，暂时也没有想到很好的方法，所以就是缝缝补补，最后差不多可以运行。具体的代码见最后。

子弹控制部分的代码：

```cpp
/*检验子弹合法性*/
int check_zidan()
{
	Zidan d;
	for (int i = 1; i <= qz.size(); i++) //这么多个子弹要检查
	{
		d = qz.front();
		qz.pop();
		//注意xy实际上是弹头坐标，左上角坐标要加上偏移量（最上面）
		if (d.x == 0 || d.y == 0) {
			if (d.count) {
				clear_zidan(d);
			}
			else {
				d.count = 1;
			}//出界
			continue;
		}
		int xx = d.x + SIZE * dirx[d.dir];
		int yy = d.y + SIZE * diry[d.dir];//确定下一步位置
		if (xx > MAXX || yy > MAXY) {
			if (d.count) {
				clear_zidan(d);
			}
			else {
				d.count = 1;
			}//出界
			continue;
		}
		int checkX1 = xx / SIZE + check_if_movex[0][d.dir];
		int checkY1 = yy / SIZE + check_if_movey[0][d.dir];//确定检查点单位位置1
		int checkX2 = xx / SIZE + check_if_movex[1][d.dir];
		int checkY2 = yy / SIZE + check_if_movey[1][d.dir];//确定检查点单位位置2
		if (flag[yy/SIZE][xx/SIZE] ==-1 &&!mat[checkY1][checkX1]&& !mat[checkY2][checkX2]) {//都为空位置，不用管
			if (d.count) {
				clear_zidan(d);
			}
			else {
				d.count = 1;
			}
			if (!tool.flag[checkY1][checkX1] && !tool.flag[checkY2][checkX2]) {//不能打在道具上
				draw_zidan(d, xx, yy);//画子弹
				//这里要画出旋转后的,这个里面的左上角坐标正确
				d.x = xx;
				d.y = yy;//更新坐标
				qz.push(d);
			}
		}
		else if(mat[checkY1][checkX1]>0 || mat[checkY2][checkX2]>0){//打到方块
			if (!((mat[checkY1][checkX1] == 1 || mat[checkY2][checkX2] == 1) && d.who != 3))
			{//有一个铁块并且等级不够就不消除,代表被挡住了
			    //消除方块
				switch (d.dir) {
				case 0:
					remove_any(-2, -1, 1, -1, d);//传递的参数是子弹信息和相对于弹头的位置(四个方块),子弹分界的左右两个
					break;
				case 1:
					remove_any(0, -2, 0, 1, d);
					break;
				case 2:
					remove_any(-2, 0, 1, 0, d);
					break;
				case 3:
					remove_any(-1, -2, -1, 1, d);
					break;
				}
			}
			if (d.count) {
				if (d.count) {
					clear_zidan(d);
				}
				else {
					d.count = 1;
				}
			}
			else {
				d.count = 1;
			}
		}
		else if (flag[yy / SIZE][xx / SIZE] == 10) {
			clearrectangle(7 * SIZE * 4, 12 * SIZE * 4, 7 * SIZE * 4 + 8 * SIZE, 12 * SIZE * 4 + 8 * SIZE);
			putimage(7 * SIZE * 4, 12 * SIZE * 4, &img[17]);
			return 0;
		}
		else {//代表炸到了坦克
		//根据编号看谁挂掉了
			if (d.who>3&&flag[yy / SIZE][xx / SIZE] == 0|| d.who <= 3 && flag[yy / SIZE][xx / SIZE]!=10)
			{
				//代表两股势力
				if (clear_tank(xx, yy, 0)) {
					//test();
					return 2;//代表游戏结束
				}
				if (d.count) {
					clear_zidan(d);
				}
				else {
					d.count = 1;
				}
			}
			else {
				if (d.count) {
					clear_zidan(d);
				}
				else {
					d.count = 1;
				}
			}
		}
	}
	return 1;
}//检验子弹合法性
```



# 对于道具的操作

>  道具的引入主要是为了增加游戏乐趣，思考之后我决定加入以下几种：炸弹（随机炸掉一个敌方坦克），冰冻（冻住敌方几秒），修复（让家的防护补满），升级（自身坦克升级）。

## 道具的生成

首先对于道具生成，一定是在多少个单更新单位时间之后，然后也不能生成在障碍物上面（我试过，会覆盖，所以就没尝试），生成原理就是随机数。

```cpp
/*添加道具*/
void add_tool()
{
	int x, y;
	bool check = 0;
	while (!check) {
		srand(rand_seed);//重置时间种子
		rand_seed = rand();//随机种子链
		x = rand() % NUMX - 2;
		y = rand() % NUMY - 2;//左上角单位坐标
		check = 1;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				if (mat[y + i][x + j]||flag[y+i][x+j]!=-1)
					check=0;//不能落在有东西的上面
			}
		}
	}
	int index = rand() % 4 + 1;//道具种类
	for (int i = 0; i < 2; i++) {
		for (int j = 0; j < 2; j++) {
			tool.flag[y + i][x + j] = index;
		}
	}
	putimage(x*SIZE+2, y*SIZE+2, &img[18+index]);
	tool.exist = 1;
}//添加道具
```



## 炸弹

炸弹就是不断在地图中生成随机数并查看是否有坦克，有就直接炸毁，考虑到程序一直找不到可能会崩溃，所以我设定了最高查找一万次，所以最终的效果就是有时候可能找不到坦克，但是找到的概率还是比较大的。

## 冰冻

冰冻的效果就是敌方坦克不动，上文我们其实已经把敌方坦克的更新封装在了函数之内，那么就只要在设定更新时间周期内不执行就好。

## 修复

修复就是把家周围的墙单独拉出来，然后重新像初始化一样执行一次，就可以完成修复。

## 升级

升级就是把自身的level+1，并且把代表自身的0号tank位置的type类型改为level+1所对应的，再次更新就可以变样。



```cpp
/*检查道具*/
int check_tool(int index) {
	//炸弹停止升级修复
	int x=0, y=0;
	int max_num = 10000;//最多寻找次数
	switch (index) {
	case 1:
		while (max_num--) {
			srand(rand_seed);//重置时间种子
			rand_seed = rand();//随机种子链
			x = rand() % NUMX;
			y = rand() % NUMY;
			if (flag[y][x]>0 && flag[y][x] != 10)
				break;
		}
		if(max_num)//表示可以找到坦克
			if (clear_tank(x*SIZE, y*SIZE, 1)) {
				return 1;
			}
		break;
	case 2:
		stop_epoch = 300;//3秒
		break;
	case 3:
		if (level < 3) {
			up_date_type(level+1,0);//更换类型
			level++;
		}
		break;
	case 4:
		repair_map();
		break;
	}
	return 0;//游戏继续
}//检查道具

```



# 最终效果

![img](https://s21.ax1x.com/2025/11/25/pZAeYJH.png)

 ![img](https://s21.ax1x.com/2025/11/25/pZAetWd.png)

# 总结

大部分的主要的程序就在上面，但很多部分都封装了一些细节，所以并不完整，只能表述大致的思路。做的不太好的就是坦克之间经常有重叠的情况，大致就是因为标记点的原因，也确实不知道该如何解决，而最终的实现代码写了千行，其中还是有不少可以优化的空间，由于水平原因，有一些功能如穿过草丛还是没有实现，不过有机会还是希望可以试试。

# **完整代码**

main.cpp

```cpp
#include"head.h"

int main() {

	game();
	return 0;
}
```



 head.h

```cpp
#define _CRT_SECURE_NO_WARNINGS 1
#include<stdio.h>
#include<algorithm>
#include <stdlib.h>
#include <time.h>
#include<graphics.h>//图形绘制库
#include<string>
#include<iostream>
#include<queue>
#include<conio.h>
#include<vector>
#include<math.h>
#include<stack>
using namespace std;

#define SIZE 15 //定义最小单元
#define NUMX 64
#define NUMY 56//定义xy轴的最小单元个数
#define MAXX NUMX*SIZE
#define MAXY NUMY*SIZE
#define PI 3.1415926
#define RIGHT 77
#define LEFT 75
#define UP 72
#define DOWN 80
#define ENTER 13
#define W 119
#define A 97
#define S 115
#define D 100
#define KONG 32
#define ESC 27//获取键值
#define KEY_DOWN(vKey) ((GetAsyncKeyState(vKey) & 0x8000) ? 1:0)//检测键盘按下函数

void game();//游戏函数入口
void PrintTankNum();

```



 tool.cpp

```cpp
#include"head.h"

int dirx[] = {0,1,0,-1};
int diry[] = {-1,0,1,0};//四个方向
int img_zidan_detx1[] = {-SIZE/2,-SIZE,-SIZE/2,0};
int img_zidan_dety1[] = {0,-SIZE/2,-SIZE,-SIZE/2};//子弹左上角相对弹头偏移量,画子弹要用
int check_if_movex[][4] = { {-1,-1,-1,0},{0,-1,0,0} };
int check_if_movey[][4] = { {0,-1,-1,-1 },{0,0,-1,0} };//表示子弹合法检查位置相对弹头偏移单位数，相邻两个都检查
int all_tank_num = 3;//其他类型坦克数量
int rest_num;//剩余坦克数量
int my_lives;//自己的坦克数量
int rand_seed;//随机种子
int level;//自身当前等级
int mat[NUMY+2][NUMX+2];//存储游戏地图
int flag[NUMY + 2][NUMX + 2];//存储坦克标记
int stop_epoch;//表示停止回合
//123分别存储三种障碍物，(-10)存储自己坦克所在位置,(-1)-(-9)存储敌方坦克位置，一个坦克占据16个单元格，左上坐标标记
int num_tank;//当前场上的坦克
int birth_wz[][2] = { {MAXX / 2 - SIZE * 12,MAXY - 4 * SIZE},			//第一个是自己的出生点，剩下的是其他坦克出生点
					{0,0},{MAXX / 2 - 16 * SIZE,0},{MAXX / 2 + 16 * SIZE,0},{MAXX - 4 * SIZE,0} };
// 第一关地图，1表示铁块，2表示红砖，3表示草地，后面是x1 y1 x2 y2 是在最小单元为2*SIZE的地图上的每个类型方块的对角线坐标
int map_1[][5] = { {2,0,2,2,8} ,{2,4,2,6,3},{1,7,0,9,2},{1,12,2,14,4},
				  {3,4,4,6,6},{2,6,4,14,6},{2,14,4,16,6},{1,2,6,4,10},
				  {2,1,10,3,11},{3,4,7,5,8},{1,5,7,7,9},{2,7,7,11,9},
				  {1,11,7,13,9},{2,13,7,15,14},{2,1,11,3,14},{1,4,3,6,4},
				  {2,6,12,7,14},{2,6,11,10,12},{2,9,12,10,14} };
int rep_map[][5] = { {2,6,12,7,14},{2,6,11,10,12},{2,9,12,10,14} };

struct Zidan {
	int who;//记录的是在坦克系列，1-3是自己，其他是其他人
	int dir;//方向
	int x;
	int y;//坐标
	bool count;//为了标记是否是第一次被更新
};
queue<Zidan>qz;

struct Tool {
	bool exist;//是否存在
	int flag[NUMY + 2][NUMX + 2];//存储道具信息
}tool;

struct Tank {
	int type;//类型
	int HP;//血量
	int dir;//方向,上下左右0123
	int x, y;//坐标
	int bef_fight;//上一次攻击到现在的间隔
	int bef_move;//上一次移动到现在的间隔
	int stop_time;//表示该坦克在几个周期内不移动
	int dir_stop_time;//表示该坦克在几个周期内不改变方向
	int move_type;//1代表优先向中间，2代表优先向下面
	int state;//当前状态
}tank[10];//最多其实就4个,0号始终是自己

// 不同坦克类型属性
struct Type {
	/*自身属性*/
	int HP;//血量
	int Speed;//速度,单位是更新周期,一个更新周期子弹移动一次
	int Power;//伤害
	int Fight_det;//攻击间隔,单位是更新周期
}type[10];
IMAGE img[40];//存储图片

/*调试*/
void test() {
	clearrectangle(0, 0, 500, 500);
}//调试

/*程序停止*/
void stop() {
	system("pause");
}//程序停止

/*全局初始化*/
void ini_all() {
	/*自身属性状态*/
	//一级
	type[1].HP = 1;
	type[1].Speed = 8;
	type[1].Power = 10;
	type[1].Fight_det = 30;
	//二级
	type[2].HP = 2;
	type[2].Speed = 7;
	type[2].Power = 15;
	type[2].Fight_det = 25;
	//三级
	type[3].HP = 3;
	type[3].Speed = 6;
	type[3].Power = 20;
	type[3].Fight_det = 20;
	/*其他坦克状态*/
	//第一种血量高的
	type[4].HP = 2;
	type[4].Speed = 10;
	type[4].Power = 10;
	type[4].Fight_det = 40;
	//第二种速度快的
	type[5].HP = 1;
	type[5].Speed = 5;
	type[5].Power = 10;
	type[5].Fight_det = 40;
	//第三种攻击快
	type[6].HP = 1;
	type[6].Speed = 10;
	type[6].Power = 10;
	type[6].Fight_det = 20;
	//第四种是的一种被打了之后的样子
	type[7].HP = 1;
	type[7].Speed = 10;
	type[7].Power = 10;
	type[7].Fight_det = 40;
	/*图片*/
	loadimage(&img[1], "./imgs/type1.png");
	loadimage(&img[2], "./imgs/type2.png");
	loadimage(&img[3], "./imgs/type3.png");
	loadimage(&img[4], "./imgs/type4.png");
	loadimage(&img[5], "./imgs/type5.png");
	loadimage(&img[6], "./imgs/type6.png");
	loadimage(&img[7], "./imgs/type7.png");//坦克图片
	loadimage(&img[11], "./imgs/铁块.png");//草地
	loadimage(&img[12], "./imgs/红砖.png");//红砖
	loadimage(&img[13], "./imgs/草地.png");//铁块
	loadimage(&img[14], "./imgs/家.png");//家
	loadimage(&img[15], "./imgs/敌方子弹.png");
	loadimage(&img[16], "./imgs/我方子弹.png");//子弹
	loadimage(&img[17], "./imgs/家爆炸.png");//家被炸掉了
	loadimage(&img[18], "./imgs/坦克爆炸.png");//坦克被炸掉了
	loadimage(&img[19], "./imgs/炸弹.png");//随机炸掉一个坦克
	loadimage(&img[20], "./imgs/停止.png");//其他坦克停止操作几个周期
	loadimage(&img[21], "./imgs/升级.png");//坦克升级
	loadimage(&img[22], "./imgs/修复.png");//修复老家
}//全局初始化

/*关卡初始化*/
void ini_every(int mp[][5], int num) {
	rest_num = 20;//敌方坦克数量
	my_lives = 3;
	tool.exist = 0;
	level = 1;//一级
	putimage(MAXX + 100, 100, &img[1]);
	putimage(MAXX + 100, 200, &img[4]);
	PrintTankNum();
	srand((unsigned)time(NULL));//调整种子
	rand_seed = rand();
	line(MAXX + 1, 0, MAXX + 1, MAXY + 1);//分割线
	putimage(7 * SIZE * 4, 12 * SIZE * 4, &img[14]);
	for (int i = 0; i <= NUMY; i++){
		for (int j = 0; j <= NUMX; j++) {
			mat[i][j] = 0;
			flag[i][j]= -1;//先初始化为-1
			tool.flag[i][j] = 0;
		}
	}
	for (int i = 12 * 4; i <= 12 * 4 + 8; i++) {
		for (int j = 7 * 4; j <= 7 * 4 + 8; j++) {
			flag[i][j] = 10;//标记为老家位置
		}
	}
	for (int i = 0; i < num; i++) {
		int k = mp[i][0];
		int x1 = mp[i][1]*4;
		int y1 = mp[i][2]*4;
		int x2 = mp[i][3]*4-1;
		int y2 = mp[i][4]*4-1;//右下角方块的左上角坐标
		for (int xx = x1; xx <= x2; xx++) {
			for (int yy = y1; yy <= y2; yy++) {
				mat[yy][xx] = k;
			}
		}
		if (k == 2) {//泥土块最小单位SIZE
			for (int xx = x1; xx <= x2; xx++) {
				for (int yy = y1; yy <= y2; yy++) {
					putimage(xx*SIZE, yy*SIZE, &img[k + 10]);
				}
			}
		}
		else {//铁块和草方块最小单位2*SIZE
			for (int xx = mp[i][1]*2; xx <= mp[i][3]*2-1; xx++) {
				for (int yy = mp[i][2]*2; yy <= mp[i][4]*2-1; yy++) {
					putimage(xx*SIZE*2 ,yy*SIZE*2, &img[k + 10]);
				}
			}
		}
	}
}//关卡初始化

/*转换数字为字符串*/
void deal(int x, char *a) {
	stack <int>sst;
	while (x) {
		sst.push(x % 10);
		x /= 10;
	}
	int l = 0;
	while (!sst.empty()) {
		a[l++] = sst.top() + ''0'';
		sst.pop();
	}
	a[l] = ''\0'';
	if (a[0] == ''\0'') {
		a[0] = ''0'';
		a[1] = ''\0'';
	}
}//转换数字为字符串

/*打印游戏结束*/
void PrintGameOver() {
	settextcolor(WHITE);
	int size = 100;//字体大小
	settextstyle(size, size, 0);
	outtextxy(90,200,"游戏结束");
}//打印游戏结束

/*通关*/
void PrintSucess() {
	settextcolor(WHITE);
	int size = 100;//字体大小
	settextstyle(size, size, 0);
	outtextxy(90, 200, "成功通关");
}//通关

/*打印坦克数量*/
void PrintTankNum() {
	char a[100];

	//计算自己坦克数量
	settextcolor(WHITE);
	settextstyle(40, 40, 0);
	outtextxy(MAXX+100+4*SIZE, 120, "X");
	deal(my_lives, a);
	//这是为了防止倒计时显示错误
	clearrectangle(MAXX + 100 + 4 * SIZE + 50, 120, MAXX + 100 + 4 * SIZE + 50 + 70, 120 + 50);
	outtextxy(MAXX + 100 + 4*SIZE+50, 120, a);

	//计算敌方坦克数量
	settextcolor(WHITE);
	settextstyle(40, 40, 0);
	outtextxy(MAXX + 100 + 4 * SIZE, 220, "X");
	deal(rest_num, a);
	clearrectangle(MAXX + 100 + 4 * SIZE + 50, 220, MAXX + 100 + 4 * SIZE + 50 + 70, 220 + 50);
	outtextxy(MAXX + 100 + 4 * SIZE + 50, 220, a);
}//打印坦克数量

/*标记坦克占据空间*/
void mark_tank(int x,int y,int mark_num) {
	for (int i = 1; i <= 3; i++) {
		for (int j = 1; j <= 3; j++) {
			flag[y / SIZE + i][x / SIZE + j] = mark_num;
		}
	}
}//标记坦克占据空间

/*更新坦克标记点*/
void update_mark(int dir, int x, int y,int index) {//新的方向和新的坐标定位点
	switch (dir) {//四种方向，上右下左
		//坦克标记点有5个
	case 0:
		for (int i = 1; i < 4; i++) {
			flag[y / SIZE + 1][x / SIZE + i] = index;
			flag[y / SIZE + 4][x / SIZE + i] = -1;//清空后面的
		}
		break;
	case 1:
		for (int i = 1; i < 4; i++) {
			flag[y / SIZE + i][x / SIZE + 3] = index;
			flag[y / SIZE + i][x / SIZE ] = -1;//清空后面的
		}
		break;
	case 2:
		for (int i = 1; i < 4; i++) {
			flag[y / SIZE ][x / SIZE + i] = -1;
			flag[y / SIZE + 3][x / SIZE + i] = index;
		}
		break;
	case 3:
		for (int i = 1; i < 4; i++) {
			flag[y / SIZE + i][x / SIZE+1] = index;
			flag[y / SIZE + i][x / SIZE + 4] = -1;//清空后面的
		}
		break;
	}
}//更新坦克标记点

/*产生一个坦克*/
Tank add_tank(int add_place,int wz) {
	//add_place表示产生位置，取值1234表示敌方坦克的出生位置，wz表示在坦克数组中的位置
	Tank t;
	t.dir = 3;
	t.bef_fight = 0;
	t.bef_move = 0;
	t.stop_time = rand()%5;
	t.dir_stop_time = 0;
	if (add_place) {//敌方坦克
		srand(rand_seed);//重置时间种子
		rand_seed = rand();//随机种子链
		int index = rand() % all_tank_num + 4;
		t.move_type = rand() % 2 + 1;
		t.type = index;
		t.HP = type[index].HP;
		t.x = birth_wz[add_place][0];
		t.y = birth_wz[add_place][1];
		t.state = 1;//表示活着
		mark_tank(t.x, t.y, wz);
	}
	else {//自己
		t.HP = type[level].HP;
		t.type = level;//通过type索引可以得到速度等信息
		t.x = birth_wz[0][0];
		t.y = birth_wz[0][1];
		mark_tank(t.x, t.y, 0);//0号标记是自己
	}
	return t;
}//产生一个坦克

/*初始化坦克队列*/
void ini_tank() {
	num_tank = 0;
	tank[0]=add_tank(0,0);//0表示产生己方坦克，其他表示其他坦克，编号index
	//在1 2 3号出生点设置敌方坦克
	for (int i = 1; i <= 3; i++) {
		num_tank++;
		tank[i]=add_tank(i, num_tank);
	}
}//初始化坦克队列

/*表示从旧的变成新的*/
void up_date_type(int new_type, int wz) {
	tank[wz].type = new_type;
	tank[wz].HP = type[new_type].HP;
}//表示从旧的变成新的

/*画一个坦克*/
void draw_a_tank(Tank t,int xx,int yy) {
	IMAGE ans;
	clearrectangle(t.x+3, t.y+3, t.x + 4 * SIZE-1, t.y + 4 * SIZE-1);
	rotateimage(&ans, &img[t.type], -PI / 2 * t.dir);//顺时针旋转dir个90度,然后赋值给ans
	putimage(xx+4, yy+4, &ans);
}//画一个坦克

/*画全部坦克*/
void draw_all_tank() {
	for (int i = 0; i <= num_tank; i++) {
		draw_a_tank(tank[i],tank[i].x,tank[i].y);
	}
}//画全部坦克

/*检查坦克位置合法性*/
int check_wz(int xx, int yy, int wz) {
	//检查是否出界
	if (xx<0 || yy<0 || xx + 4 * SIZE>MAXX || yy + 4 * SIZE>MAXY||wz&&tool.flag[yy/SIZE][xx/SIZE]) {
		return 0;
	}
	//检查坦克所在位置是否有障碍物以及道具
	int tool_x = -1, tool_y = -1;
	for (int i = 0; i < 4; i++) {
		for (int j = 0; j < 4; j++) {
			if (mat[yy / SIZE + i][xx / SIZE + j] > 0
				|| flag[yy / SIZE + i][xx / SIZE + j] >= 0 && flag[yy / SIZE + i][xx / SIZE + j] != wz)//已经有障碍物或者有其他坦克
				return 0;
			if (tool.flag[yy / SIZE + i][xx / SIZE + j]) {
				if (wz)return 0;
				else {
					tool_x = xx / SIZE + j;
					tool_y = yy / SIZE + i;
				}
			}
		}
	}
	//检查无障碍物并且有道具
	int index = tool.flag[tool_y][tool_x];
	if (tool_x != -1) {
		//清空标记
		if (tool_x > 0 && tool.flag[tool_y][tool_x - 1])
			tool_x--;
		if (tool_y > 0 && tool.flag[tool_y - 1][tool_x])
			tool_y--;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				tool.flag[tool_y + i][tool_x + j] = 0;
			}
		}
		clearrectangle(tool_x*SIZE, tool_y*SIZE, tool_x*SIZE + 2 * SIZE, tool_y*SIZE + 2 * SIZE);
		draw_a_tank(tank[0], tank[0].x, tank[0].y);//重画坦克
		return index;
	}
	return 10;
}//检查坦克位置合法性

/*清除坦克*/
int clear_tank(int x,int y,int type_of) {
	//test();
	int xx = x / SIZE;
	int yy = y / SIZE;//记录消除单位坐标
	int bh = flag[yy][xx];//坦克在坦克队列中的编号，0是自己
	if (type_of) {//表示是炸弹的魔法伤害
		tank[bh].HP = 0;
	}
	else {//否则就是普通子弹
		tank[bh].HP--;
	}
	if (tank[bh].HP) {
		if (bh) {
			up_date_type(7, bh);//两条命的敌人
		}
		else {
			up_date_type(level-1, bh);//自身
		}
		return 0;//表示还没挂掉,直接返回
	}
	//向左上角追溯
	while (flag[yy][xx] == bh) {
		xx--;
	}
	xx++;//为了接下来还可以查找y,因为找到的位置x实际上是没有flag标记的
	while (flag[yy][xx] == bh) {
		yy--;
	}
	yy++;//统一，这样左上角坐标就是[y-1][x-1]
	//清除坦克
	clearrectangle((xx-1)*SIZE, (yy-1)*SIZE, (xx-1)*SIZE + 4 * SIZE - 1, (yy-1)*SIZE + 4 * SIZE - 1);
	putimage((xx - 1)*SIZE + 2, (yy - 1)*SIZE + 2, &img[18]);
	Sleep(100);
	clearrectangle((xx - 1)*SIZE, (yy - 1)*SIZE, (xx - 1)*SIZE + 4 * SIZE - 1, (yy - 1)*SIZE + 4 * SIZE - 1);
	//清空标记点
	for (int i = yy ; i <yy+ 3; i++) {
		for (int j = xx ; j < xx + 3; j++) {
			flag[i][j] = -1;
		}
	}
	if (bh == 0) {//代表自己被打死了
		level = 1;
		tank[0]=add_tank(0, 0);
		draw_a_tank(tank[0], tank[0].x, tank[0].y);
		my_lives--;
	}
	else {//其他坦克
		rest_num--;
		if (!rest_num)//坦克清空
		{
			return 1;//表示游戏结束(打完了)
		}
		else {
			if (rest_num < 3) {
				tank[bh].state = 0;//当前位置没有坦克
			}
			else {
				//产生一个新坦克
				srand(rand_seed);//重置时间种子
				rand_seed = rand();//随机种子链
				int rand_place;
				while (1) {
					srand(rand_seed);//重置时间种子
					rand_seed = rand();//随机种子链
					rand_place = rand() % 4 + 1;
					if (check_wz(birth_wz[rand_place][0], birth_wz[rand_place][1], bh))
						break;
				}
				tank[bh] = add_tank(rand_place, bh);
			}
		}
	}
	PrintTankNum();
	return 0;//游戏继续
}//清除坦克

/*消除该区域内的物体*/
void remove_any(int detx1,int dety1, int detx2, int dety2,Zidan d)
{
	int x = d.x;
	int y = d.y;
	//计算起始xy坐标
	int stx = d.x / SIZE;
	int sty = d.y / SIZE;
	//控制区间
	if (dety1 == dety2) //横向排列
	{
		//先判断有空的情况
		int index = 2;
		for (int i = -1; i <= 1; i++)//找到跳变点并退出
		{
			if (mat[sty + dety1][stx + i] != mat[sty + dety1][stx + i - 1])
			{
				//要保证不是跟空比较
				index = i;
				break;
			}
		}
		if (!(mat[sty + dety1][stx + index] * mat[sty + dety1][stx + index - 1])) {
			//有空
			if (mat[sty + dety1][stx + index] + mat[sty + dety1][stx + index - 1] == 3)//一边是草
			{
				return;
			}
			index++;
			while (mat[sty + dety1][stx + index] == mat[sty + dety1][stx + index - 1]&&index<2)
			{
				index++;
			}
		}
		if (index!=2) {//表明有变化,那么可能是铁+草，铁+泥土，草+泥土任意组合
			//逐个讨论
			//带草 3
			if (mat[sty + dety1][stx + index] == 3 || mat[sty + dety1][stx + index - 1] == 3)//修正消除位置，草不能被打掉
			{
				if (mat[sty + dety1][stx + index-1] == 3)//代表左边是草
				{
					detx1 = index;//修改左边界
				}
				else
				{
					detx2 = index-1;//修改右边界
				}
			}
			else //一般坦克打的，不能消除铁，要判掉
			{
				if (mat[sty + dety1][stx + index-1] == 1)//代表左边是铁
				{
					detx1 = index;//修改左边界
				}
				else
				{
					detx2 = index - 1;//修改右边界
				}
			}
		}
		else if(mat[sty + dety1][stx + detx1] == 3){
			return;//全是草不考虑
		}
	}
	else //纵向排列
	{
		int index = 2;
		for (int i = -1; i <= 1; i++)//找到跳变点并退出
		{
			if (mat[sty + i][stx + detx1] != mat[sty + i-1][stx + detx1])
			{
				index = i;
				break;
			}
		}
		if (!(mat[sty + index - 1][stx + detx1] * mat[sty + index][stx + detx1])) {
			//有空
			if (mat[sty + index][stx + detx1] + mat[sty + index - 1][stx + detx1] == 3)//空+草
				return;
			index++;
			while (mat[sty + index][stx + detx1] == mat[sty + index - 1][stx + detx1] && index < 2)
			{
				index++;
			}
		}
		if (index!=2) {//表明有变化,那么可能是铁+草，铁+泥土，草+泥土任意组合
			//逐个讨论
			//带草 3
			if (mat[sty + index -1][stx + detx1] == 3 || mat[sty + index][stx + detx1] == 3)//修正消除位置，草不能被打掉
			{
				if (mat[sty + index-1][stx + detx1] == 3)//代表上是草
				{
					dety1 = index;//修改上边界
				}
				else
				{
					dety2 = index - 1;//修改下边界
				}
			}
			else //一般坦克打的，不能消除铁，要判掉
			{
				if (mat[sty + index -1][stx + detx1] == 1)//代表上边是铁
				{
					dety1 = index;//修改上边界
				}
				else
				{
					dety2 = index - 1;//修改下边界
				}
			}
		}
		else if (mat[sty + dety1][stx + detx1] == 3) {
			return;//全是草不考虑
		}
	}
	if (d.who != 3 && mat[y / SIZE + dety1][x / SIZE + detx1] == 1) {//修正之后指向铁，是铁+草的情况，上面未修正完全
		return;//无法消除
	}
	//计算消除矩阵坐标
	int x1 = x + detx1*SIZE;
	int y1 = y + dety1*SIZE;
	int x2 = x + detx2*SIZE+SIZE;
	int y2 = y + dety2*SIZE+SIZE;
	clearrectangle(x1, y1, x2-1, y2-1);
	for (int i = x / SIZE + detx1; i <= x / SIZE + detx2; i++)
	{
		for (int j = y / SIZE + dety1; j <= y / SIZE + dety2; j++) {
			mat[j][i] = 0;//清除标记
		}
	}
}//消除该区域内的物体

/*画一个子弹*/
void draw_zidan(Zidan d, int xx, int yy) {
	IMAGE ans;
	rotateimage(&ans, &img[(d.who<=3)+15], -PI / 2 * d.dir);//顺时针旋转dir个90度,然后赋值给ans
	putimage(xx + img_zidan_detx1[d.dir]+1, yy + img_zidan_dety1[d.dir]+1, &ans);//旋转后传参
}//画一个子弹

/*清除子弹*/
void clear_zidan(Zidan d) {
	int x1 = d.x + img_zidan_detx1[d.dir];
	int y1 = d.y + img_zidan_dety1[d.dir];
	int x2 = d.x + img_zidan_detx1[d.dir] + SIZE-1;
	int y2 = d.y + img_zidan_dety1[d.dir] + SIZE-1;
	clearrectangle(x1, y1, x2, y2);
	//先算出左上角坐标然后推算右下角坐标
}//清除子弹

/*检验子弹合法性*/
int check_zidan()
{
	Zidan d;
	for (int i = 1; i <= qz.size(); i++) //这么多个子弹要检查
	{
		d = qz.front();
		qz.pop();
		//注意xy实际上是弹头坐标，左上角坐标要加上偏移量（最上面）
		if (d.x == 0 || d.y == 0) {
			if (d.count) {
				clear_zidan(d);
			}
			else {
				d.count = 1;
			}//出界
			continue;
		}
		int xx = d.x + SIZE * dirx[d.dir];
		int yy = d.y + SIZE * diry[d.dir];//确定下一步位置
		if (xx > MAXX || yy > MAXY) {
			if (d.count) {
				clear_zidan(d);
			}
			else {
				d.count = 1;
			}//出界
			continue;
		}
		int checkX1 = xx / SIZE + check_if_movex[0][d.dir];
		int checkY1 = yy / SIZE + check_if_movey[0][d.dir];//确定检查点单位位置1
		int checkX2 = xx / SIZE + check_if_movex[1][d.dir];
		int checkY2 = yy / SIZE + check_if_movey[1][d.dir];//确定检查点单位位置2
		if (flag[yy/SIZE][xx/SIZE] ==-1 &&!mat[checkY1][checkX1]&& !mat[checkY2][checkX2]) {//都为空位置，不用管
			if (d.count) {
				clear_zidan(d);
			}
			else {
				d.count = 1;
			}
			if (!tool.flag[checkY1][checkX1] && !tool.flag[checkY2][checkX2]) {//不能打在道具上
				draw_zidan(d, xx, yy);//画子弹
				//这里要画出旋转后的,这个里面的左上角坐标正确
				d.x = xx;
				d.y = yy;//更新坐标
				qz.push(d);
			}
		}
		else if(mat[checkY1][checkX1]>0 || mat[checkY2][checkX2]>0){//打到方块
			if (!((mat[checkY1][checkX1] == 1 || mat[checkY2][checkX2] == 1) && d.who != 3))
			{//有一个铁块并且等级不够就不消除,代表被挡住了
			    //消除方块
				switch (d.dir) {
				case 0:
					remove_any(-2, -1, 1, -1, d);//传递的参数是子弹信息和相对于弹头的位置(四个方块),子弹分界的左右两个
					break;
				case 1:
					remove_any(0, -2, 0, 1, d);
					break;
				case 2:
					remove_any(-2, 0, 1, 0, d);
					break;
				case 3:
					remove_any(-1, -2, -1, 1, d);
					break;
				}
			}
			if (d.count) {
				if (d.count) {
					clear_zidan(d);
				}
				else {
					d.count = 1;
				}
			}
			else {
				d.count = 1;
			}
		}
		else if (flag[yy / SIZE][xx / SIZE] == 10) {
			clearrectangle(7 * SIZE * 4, 12 * SIZE * 4, 7 * SIZE * 4 + 8 * SIZE, 12 * SIZE * 4 + 8 * SIZE);
			putimage(7 * SIZE * 4, 12 * SIZE * 4, &img[17]);
			return 0;
		}
		else {//代表炸到了坦克
		//根据编号看谁挂掉了
			if (d.who>3&&flag[yy / SIZE][xx / SIZE] == 0|| d.who <= 3 && flag[yy / SIZE][xx / SIZE]!=10)
			{
				//代表两股势力
				if (clear_tank(xx, yy, 0)) {
					//test();
					return 2;//代表游戏结束
				}
				if (d.count) {
					clear_zidan(d);
				}
				else {
					d.count = 1;
				}
			}
			else {
				if (d.count) {
					clear_zidan(d);
				}
				else {
					d.count = 1;
				}
			}
		}
	}
	return 1;
}//检验子弹合法性

/*根据坦克产生一个子弹*/
void add_zidan(Tank t) {
	int tmp_type = t.type;//可以取到子弹的伤害
	Zidan d;
	d.dir = t.dir;
	int xx, yy;
	switch (t.dir) {//根据方向和坦克的左上角坐标确定子弹占据空间
	case 0:
		xx = t.x + 2 * SIZE;
		yy = t.y;
		break;
	case 1:
		xx = t.x + 4 * SIZE;
		yy = t.y + 2 * SIZE;
		break;
	case 2:
		xx = t.x + 2 * SIZE;
		yy = t.y + 4 * SIZE;
		break;
	case 3:
		xx = t.x;
		yy = t.y + 2 * SIZE;
		break;
	}
	d.x = xx;
	d.y = yy;
	d.who = t.type;//123都是自己的形态，代表是自己,否则是对方的
	d.count = 0;
	qz.push(d);
}//根据坦克产生一个子弹

/*修复老家*/
void repair_map() {
	int num = sizeof(rep_map) / sizeof(rep_map[0]);
	for (int i = 0; i < num; i++) {
		int k = rep_map[i][0];
		int x1 = rep_map[i][1] * 4;
		int y1 = rep_map[i][2] * 4;
		int x2 = rep_map[i][3] * 4 - 1;
		int y2 = rep_map[i][4] * 4 - 1;//右下角方块的左上角坐标
		for (int xx = x1; xx <= x2; xx++) {
			for (int yy = y1; yy <= y2; yy++) {
				mat[yy][xx] = k;
			}
		}
		for (int xx = x1; xx <= x2; xx++) {
			for (int yy = y1; yy <= y2; yy++) {
				putimage(xx*SIZE, yy*SIZE, &img[k + 10]);
			}
		}
	}
}//修复老家

/*检查道具*/
int check_tool(int index) {
	//炸弹停止升级修复
	int x=0, y=0;
	int max_num = 10000;//最多寻找次数
	switch (index) {
	case 1:
		while (max_num--) {
			srand(rand_seed);//重置时间种子
			rand_seed = rand();//随机种子链
			x = rand() % NUMX;
			y = rand() % NUMY;
			if (flag[y][x]>0 && flag[y][x] != 10)
				break;
		}
		if(max_num)//表示可以找到坦克
			if (clear_tank(x*SIZE, y*SIZE, 1)) {
				return 1;
			}
		break;
	case 2:
		stop_epoch = 300;//3秒
		break;
	case 3:
		if (level < 3) {
			up_date_type(level+1,0);//更换类型
			level++;
		}
		break;
	case 4:
		repair_map();
		break;
	}
	return 0;//游戏继续
}//检查道具

/*四个方向*/
void control_dir(int now_dir) {
	if (tank[0].dir != now_dir) //原来不是在这个
	{
		tank[0].dir = now_dir;
		draw_a_tank(tank[0], tank[0].x, tank[0].y);
		tank[0].bef_move = now_dir;//更新
	}
	else if (tank[0].bef_move >= type[tank[0].type].Speed)//可以移动
	{
		int xx = tank[0].x + dirx[tank[0].dir] * SIZE;
		int yy = tank[0].y + diry[tank[0].dir] * SIZE;//坐标
		int check = check_wz(xx, yy, 0);
		if (check) { //检查位置
			draw_a_tank(tank[0], xx, yy);//xx,yy表示更新后的位置
			update_mark(tank[0].dir,xx,yy,0);//更新坦克位置信息
			tank[0].x = xx;
			tank[0].y = yy;
			tank[0].bef_move = 0;//更新时间
			if (check != 10) {
				check_tool(check);//检查道具
				tool.exist = 0;
			}
		}
	}
}//四个按钮

/*控制其他坦克*/
void check_tank()
{
	for (int i = 1; i <= num_tank; i++) {//依次取出坦克
		if (!tank[i].state)continue;//已经没有了
		if (tank[i].bef_move >= type[tank[i].type].Speed) {
			//可能停留在原地的
			if (tank[i].stop_time) {
				tank[i].stop_time--;
				if (tank[i].bef_fight >= type[tank[i].type].Fight_det) {
					srand(rand_seed);//重置时间种子
					rand_seed = rand();//随机种子链
					int state = rand() % 100;
					if (state <= 50) {
						tank[i].bef_fight = 0;
						add_zidan(tank[i]);//添加一个子弹
					}
					else {
						tank[i].bef_fight = 0;
					}
				}
			}
			//表示要移动的
			else if (tank[i].dir_stop_time) {
				int xx = tank[i].x + dirx[tank[i].dir] * SIZE;
				int yy = tank[i].y + diry[tank[i].dir] * SIZE;//坐标
				if (check_wz(xx, yy, i))
				{
					draw_a_tank(tank[i], xx, yy);//xx,yy表示更新后的位置
					update_mark(tank[i].dir, xx, yy, i);//更新坦克位置信息
					tank[i].x = xx;
					tank[i].y = yy;
					tank[i].bef_move = 0;//更新时间
					tank[i].dir_stop_time--;
				}
				else
				{
					//先打一发
					if (tank[i].bef_fight >= type[tank[i].type].Fight_det) {
						tank[i].bef_fight = 0;
						add_zidan(tank[i]);//添加一个子弹
					}
					srand(rand_seed);//重置时间种子
					rand_seed = rand();//随机种子链
					int state = rand() % 100;
					tank[i].dir_stop_time = state % 10+1;//方向锁定次数
					if (state % 3==1) {
						tank[i].dir = (tank[i].dir + 1) % 4;
						if(state%2)
							tank[i].stop_time = state*2;//可能几个回合之内停在原地
					}
					else if(state % 3 == 2){
						tank[i].dir = (tank[i].dir - 1 + 4) % 4;
						if(state%2)
							tank[i].stop_time = state*2;//可能几个回合之内停在原地
					}
					else {
						tank[i].dir = (tank[i].dir + 2) % 4;//反向
					}
					draw_a_tank(tank[i], tank[i].x, tank[i].y);
				}
			}
			else {
				//换方向默认优先向着中间目标
				srand(rand_seed);//重置时间种子
				rand_seed = rand();//随机种子链
				int state = rand() % 100;
				//根据移动类型优化路径
				if (tank[i].move_type == 1) {
					if (abs(tank[i].x - MAXX / 2) * 2 / MAXX * 100 > state) {//距离中间远
						if (tank[i].x - MAXX / 2 >= 0) {
							tank[i].dir = 3;//左边
						}
						else {
							tank[i].dir = 1;//右边
						}
					}
					else if (abs(tank[i].y - MAXY) / MAXY * 100 > state) {//距离下面远
						tank[i].dir = 2;//向下走
					}
					else {
						tank[i].dir = state % 4;//随机换向
					}
				}
				else {
					if (abs(tank[i].y - MAXY) / MAXY * 100 > state) {//距离下面远
						tank[i].dir = 2;//向下走
					}
					else if (abs(tank[i].x - MAXX / 2) * 2 / MAXX * 100 > state) {//距离中间远
						if (tank[i].x - MAXX / 2 >= 0) {
							tank[i].dir = 3;//左边
						}
						else {
							tank[i].dir = 1;//右边
						}
					}
					else {
						tank[i].dir = state % 4;//随机换向
					}
				}
				tank[i].dir_stop_time = state % 10+1;//方向锁定次数
				draw_a_tank(tank[i], tank[i].x, tank[i].y);
			}
		}
		//控制射击
		if (tank[i].bef_fight >= type[tank[i].type].Fight_det) {
			srand(rand_seed);//重置时间种子
			rand_seed = rand();//随机种子链
			int state = rand() % 100;
			if (state <= 50) {
				tank[i].bef_fight = 0;
				add_zidan(tank[i]);//添加一个子弹
			}
			else {
				tank[i].bef_fight = 0;
			}
		}
		tank[i].bef_fight = (tank[i].bef_fight + 1) % 10000;//距离上次发射时间
		tank[i].bef_move = (tank[i].bef_move + 1) % 10000;//距离上次移动时间
	}
}//控制其他坦克

/*添加道具*/
void add_tool()
{
	int x, y;
	bool check = 0;
	while (!check) {
		srand(rand_seed);//重置时间种子
		rand_seed = rand();//随机种子链
		x = rand() % NUMX - 2;
		y = rand() % NUMY - 2;//左上角单位坐标
		check = 1;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				if (mat[y + i][x + j]||flag[y+i][x+j]!=-1)
					check=0;//不能落在有东西的上面
			}
		}
	}
	int index = rand() % 4 + 1;//道具种类
	//y = 40;
	//x = 30;
	//index = 4;
	for (int i = 0; i < 2; i++) {
		for (int j = 0; j < 2; j++) {
			tool.flag[y + i][x + j] = index;
		}
	}
	putimage(x*SIZE+2, y*SIZE+2, &img[18+index]);
	tool.exist = 1;
}//添加道具

/*开始游戏*/
void begin() {
	int num_t = 0;
	while (1) {
		if (KEY_DOWN(VK_UP)) {
			control_dir(0);
		}
		if (KEY_DOWN(VK_RIGHT)) {
			control_dir(1);
		}
		if (KEY_DOWN(VK_DOWN)) {
			control_dir(2);
		}
		if (KEY_DOWN(VK_LEFT)) {
			control_dir(3);
		}
		if (KEY_DOWN(VK_SPACE)) {
			if (tank[0].bef_fight >= type[tank[0].type].Fight_det) {//至少要过几个时间段才可以发射子弹
				tank[0].bef_fight = 0;
				add_zidan(tank[0]);//添加一个子弹
			}
		}
		if (KEY_DOWN(VK_ESCAPE)) {
			stop();
		}
		if (stop_epoch) {
			stop_epoch--;
		}
		else {
			check_tank();//其他坦克
		}
		Sleep(10);//更新频率
		int now_state = check_zidan();
		if (now_state == 2) //成功通关
		{
			PrintSucess();
			return;
		}
		else if (!now_state || my_lives == 0)//清除无效子弹，检查是否有坦克炸毁
		{
			PrintGameOver();
			return;
		}
		tank[0].bef_fight = (tank[0].bef_fight + 1) % 10000;//距离上次发射时间
		tank[0].bef_move = (tank[0].bef_move + 1) % 10000;//距离上次移动时间
		num_t = (num_t + 1) % 10001;
		if (num_t % 1000 == 0) {//要考虑刷新道具了
			num_t = 0;
			if (!tool.exist) {
				add_tool();
				tool.exist = 1;
			}
		}
	}
}//开始游戏

void game() {
	initgraph(MAXX + 1 + 400, MAXY + 1,SHOWCONSOLE);
	ini_all();//全局初始化函数,一次游戏只初始化一次(区别于关卡初始化)

	while (1) {
		Sleep(10);
		settextcolor(WHITE);
		settextstyle(400, 40,0);
		outtextxy(300, 300, "点击ENTER开始游戏...");
		if (KEY_DOWN(13)) {//点击enter进入
			//下面先写第一关
			cleardevice();
			int num = sizeof(map_1) / sizeof(map_1[0]);
			ini_every(map_1, num);//关卡初始化
			ini_tank();//初始化出坦克
			draw_all_tank();//画坦克
			begin();//游戏开始
			while (1) {
				Sleep(10);
				if (KEY_DOWN(VK_ESCAPE)) {
					cleardevice();
					break;
				}
			}
		}
	}
}
```

',0,'2022-09-23 20:15:27','2022-09-23 20:15:27');

INSERT INTO article_detail(article_id,version,content,deleted,create_time,update_time) VALUES(4,1,_utf8mb4'>  **目录**
>
> [素材整理](#素材整理)
>
> [穿越草地](#穿越草地)
>
> [坦克穿越草地](#坦克穿越草地)
>
> [子弹穿越草地](#子弹穿越草地)
>
> [传送门](#传送门)
>
> [判定形式](#判定形式)
>
> [生成传送门](#生成传送门)
>
> [传送坦克](#传送坦克)
>
> [关卡模式](#关卡模式)
>
> [效果展示](#效果展示)
>
> [总结](#编辑总结)
>
> [**完整代码**](#**完整代码**)

*上一篇坦克大战居然意外获得了一些关注，正好最近也完善了一些功能，同时也加入了一些自己想到的新元素，主要是关于穿越草地，加入传送门以及关卡模式的一些开发，所以就再发一篇补充来做个完结。*

# 素材整理

> 素材整理主要是针对素材的一些改变，主要是透明背景需要的，同时也有新加入的元素所需要的。

 在上一篇我在结尾提到了最后的一些小遗憾，里面提到了穿越草地的设定，也就是说草地实际上是可以被子弹和坦克穿过的，如下图：

![img](https://s21.ax1x.com/2025/11/25/pZAedyt.png)



但是为什么之前我没有做呢，就是因为**EasyX库导入图片png图片，透明背景作为黑色处理**，所以如果先放了一个图片，再往上覆盖一层，那么下面的就会被覆盖掉，哪怕草丛的背景是空白的，也还是会把坦克直接覆盖掉，为了解决这个问题，我就去找了一些博客，找到了解决方案（参考链接：[解决easyx插入透明图片的问题_fewhite的博客-CSDN博客_easyx导入图片](https://blog.csdn.net/fewhite/article/details/126045009)），大致就是利用了异或和同或的思路，用ps整理了素材库如下（有一些是方块的，或者基本不需要透明背景的，就没有处理）：

![img](https://s21.ax1x.com/2025/11/25/pZAewOP.png)

 然后用这样的方式导入图片（[17]和[18]就是对应图片需要的两张图片，如草丛_1和草丛_2)：

```cpp
putimage(xx*SIZE * 2, yy*SIZE * 2, &img[17], SRCAND);
putimage(xx*SIZE * 2, yy*SIZE * 2, &img[18], SRCPAINT);
```



 这样放上的草丛，周围没有草的地方就不会挡住坦克，实现一个部分遮挡的效果，如上示意图所示。

有一些新的图片，比如传送门是传送门模块所需要的图片。

**说明一下所有图片素材的尺寸大小,单位SIZE，泥土的最小单位，也就是所谓的单位长度，实际大小\*15：**

泥土：1，坦克和坦克爆炸：4，草丛：2，铁块：2

传送门：4*1，家和家爆炸：8，各种道具：2，子弹：1

实际上我为了清除方便很多大小都减了一点点，比如2*SIZE应该是30，我就给变成了28，看是看不出的,也没什么区别，实际上不减少也没区别。

改变尺寸用下面这个脚本（语言：python）：

```python
import os
from PIL import Image  # 批量对一批图片更改大小

base_url = r''D:\vs2017\坦克大战\坦克大战\ini_size''  # 图片位置
base_save = r''D:\vs2017\坦克大战\坦克大战\tmp'' # 保存位置
target_width = 14
target_height = 14  # 目标大小
isExists = os.path.exists(base_save)  # 创建属于这一张图片的文件夹
if not isExists:
    os.makedirs(base_save)

img_list = []
for file in os.listdir(base_url):  # 图片位置
    img_list.append(file)

for imgs in img_list:
    img = Image.open(base_url +''/''+ imgs)  # 读取图片

    pic = img.resize((target_width, target_height))  # 重置大小 （长，宽）

    pic.save(base_save +''/''+ imgs)  # 保存（默认路径为原图片位置+原图片名[也就是相当于覆盖]）
print(''写入完成'')
```



# 穿越草地

> 草地实际上是原版里面一个经典的方块类型，坦克和子弹都可以从其中穿过，并且坦克在草丛中具有一定迷惑性，从而也增加了游戏的不确定性和趣味性。

## 坦克穿越草地

坦克穿越草地，实际上就是你**把旧坦克擦掉重新画一个的时候，如果你在清除坦克的同时也清除了草地，那么就重新补充一个草地方块上去**。

第一步就**是在清除坦克的时候（我的执行清除坦克命令是在画坦克模块里面）做一个判定，看有没有触及到草方块**，就是看放置坦克的4*4位置的左上角看，有没有地方是有草方块的，有就直接返回有，然后进入下一步。

```cpp
/*检查是否穿过草丛*/
bool CheckGrass(int xx,int yy) {
	for (int i = 0; i < 4; i++) {
		for (int j = 0; j < 4; j++) {
			if (mat[yy / SIZE + i][xx / SIZE + j] == 3) {
				return 1;//有穿过草地
			}
		}
	}
	return 0;
}//检查是否穿过草丛
```



第二步就是 **如果有了，那么需要在正确位置重新放上草地**。为什么要说是正确的位置，因为草方块实际上占据了两个单位长度(2*SIZE * 2*SIZE)（这也是为了好看，不然一个单位长度大小很丑，也不太符合原版的设定），所以如果走一遍判定，然后发现一个草方块被清除就放上一个，那么最后会有图层的重叠错位，这是我们不想看到的，所以我们需要找到被清除的位置的正确填补位置，然后放上草方块。

![img](https://s21.ax1x.com/2025/11/25/pZAeaQI.png)



如上图，每个点都是一个单位坐标，可以放置图片。实际上任意一个点位置发现草地被清除了，都应该回溯到左上角黄色点位置放置草丛图片，所以现在的问题就变成了：对于任意一个点，你知道这个是草丛并且被清除了，那么如何找到左上角的点？ 我想来想去，要是标记的话工作量还是比较大，于是我选了一个投机的方法，也就是**让每一个一个黄色点（放置草地位置）都落在横坐标和纵坐标均为偶数的位置**（所以一开始无心的规定障碍物坐标判定点在4*SIZE中的坐标位还派上了用场，因为每个点的对应坐标都要*4），这样对于四个点中的任一点缺失，我就可以很容易判断哪个是草丛的放置点，从而很好完成复原。实际的代码还要考虑不少细节问题，调试还是有点恶心...

代码如下：xx,yy是当前清除坦克的16个标记点的最左上角坐标（原始坐标）

```cpp
/*修复草地---坦克经过草地*/
void add_grass(int xx, int yy) {
	bool tmp_map[4][4];
	int detx = 0, dety = 0;//左上角
	int last_x, last_y;//右下角
	bool if_first = 0;//标记是否是第一次出现，是为了标记左上角坐标
	for (int i = 0; i < 4; i++) {
		for (int j = 0; j < 4; j++) {
			tmp_map[i][j] = (mat[yy / SIZE + i][xx / SIZE + j] == 3);//有坦克并且经过
			if (!if_first&&mat[yy / SIZE + i][xx / SIZE + j] == 3) {
				detx = j;
				dety = i;
				if_first = 1;
			}
			if (tmp_map[i][j]) {
				last_x = j;
				last_y = i;
			}
		}
	}
	detx -= (xx / SIZE + detx) % 2;
	dety -= (yy / SIZE + dety) % 2;
	for (int i = dety; i <= last_y; i += 2) {
		for (int j = detx; j <= last_x; j += 2) {
			if (i < 0&&tmp_map[0][j] || j < 0&&tmp_map[i][0] || tmp_map[i][j]) {//需要放上图片
				putimage(xx + j * SIZE, yy + i * SIZE, &img[17], SRCAND);
				putimage(xx + j * SIZE, yy + i * SIZE, &img[18], SRCPAINT);
			}
		}
	}
}//修复草地---坦克经过草地
```



## 子弹穿越草地

> 接下来是子弹穿越草地，子弹穿越草地实际上就是要考虑穿越过程中如果碰到了草地也应该修复草地。

 子弹实际上飞在一个个单元线上：弹头坐标（N1*SIZE,N2*SIZE），所以子弹穿越过程中两边的草丛都要考虑，也就是如下的情况：

![img](https://s21.ax1x.com/2025/11/26/pZAqC3d.png)

这时候子弹会把两边的草丛都消除，所以需要把两边都修复，其他时候就看穿过的子弹消除谁，找到对应的正确放置点重新补上草丛就好，大致需要分方向考虑。

```cpp
/*检查修补子弹打掉的草*/
void check_add_zidan_grass(Zidan d) {
	//推算左上角坐标并转化为单位形式
	int left_up_x = (d.x + img_zidan_detx1[d.dir]) / SIZE;
	int left_up_y = (d.y + img_zidan_dety1[d.dir]) / SIZE;
	left_up_x -= left_up_x % 2;
	left_up_y -= left_up_y % 2;//转换为草方块的定位点位置
	if (!(d.dir % 2)) {//上下
		int if_ou = 1-(d.x) / SIZE % 2;//如果子弹对称轴位于偶数线，那么就需要补充两张草
		//先判断要不要补充
		if (mat[left_up_y][left_up_x] == 3)//草
		{
			putimage(left_up_x*	SIZE, left_up_y*SIZE,&img[17], SRCAND);
			putimage(left_up_x*	SIZE, left_up_y*SIZE,&img[18], SRCPAINT);
		}
		if (if_ou&&mat[left_up_y][left_up_x + 2]==3)//看右边
		{
			putimage((left_up_x + 2) * SIZE, left_up_y * SIZE, &img[17], SRCAND);
			putimage((left_up_x + 2) * SIZE, left_up_y * SIZE, &img[18], SRCPAINT);
		}
	}
	else {//左右
		int if_ou = 1 - (d.y) / SIZE % 2;//如果子弹对称轴位于偶数线，那么就需要补充两张草
		//先判断要不要补充
		if (mat[left_up_y][left_up_x] == 3)//草
		{
			putimage(left_up_x*	SIZE, left_up_y*SIZE, &img[17], SRCAND);
			putimage(left_up_x*	SIZE, left_up_y*SIZE, &img[18], SRCPAINT);
		}
		if (if_ou&&mat[left_up_y+2][left_up_x] == 3)//看右边
		{
			putimage(left_up_x  * SIZE, (left_up_y+2) * SIZE, &img[17], SRCAND);
			putimage(left_up_x  * SIZE, (left_up_y+2) * SIZE, &img[18], SRCPAINT);
		}
	}
}//检查修补子弹打掉的草
```



#  传送门

> 传送门模块是原版里面没有的，我是受到植物大战僵尸小游戏的启发，觉得加入这个元素应该会比较有意思。
>
> 传送门也就是坦克从一边进去，然后就可以从另外一个传送门出来。

##  判定形式

首先说一下我设定的传送门的形式。传送门分为两种，一种是横着的，也就是左右向，一种是竖着的，也就是上下向，如下图：

![img](https://s21.ax1x.com/2025/11/26/pZAqPgA.png)



 任意一种传送门，尺寸大小是4*1(单位SIZE)，这也是考虑之后的，因为太大合法生成位置不好找，太小不好判定进入方式，所以就设定为和坦克等大，这样坦克向下图这样**对准**进去之后，就可以完成传送，**其他形式，传送门均为障碍物，不可以传送子弹，子弹也不可以穿过**。



![img](https://s21.ax1x.com/2025/11/26/pZAq99H.png)





下面是对弹可能否合法进入的判断代码，必须是完全对齐才可以进去，也需要对方向进行讨论：

```cpp
struct Door {
	int num;
	int x1,y1,x2,y2;//两个传送门的坐标
	int arrx1, arrx2, arry1, arry2;//两个传送门对应的到达坐标
	int dir1, dir2;//出传送门的方向，可额能没用到?
	int type;//传送门类型，上下 0/左右 1
	int flag[NUMY + 2][NUMX + 2];//存储信息
}door;//关于传送门的结构体

/*检查关于传送门*/
int CheckDoor(Tank t,int x,int y)
{
	//xy是下一步的单位坐标
	//检查是否进入传送门，只有正对传送门才能进入
	if (1 - door.type == t.dir % 2) {
		if (t.dir % 2) {//左右
			if (y != door.y2&&y != door.y1)
				return 0;//不能进入
			if (y==door.y1&&(t.dir == 1 && x+ 3 == door.x1|| t.dir == 3 && x == door.x1)) {
				return 1;//从第一个进入
			}
			else if(y==door.y2&&(t.dir == 1 && x + 3 == door.x2 || t.dir == 3 && x == door.x2)){
				return 2;//从第二个进入
			}
		}
		else {//上下
			if (x != door.x2&&x != door.x1)
				return 0;
			if (x==door.x1&&(t.dir == 2 && y + 3 == door.y1 || t.dir == 0 && y == door.y1)) {
				return 1;//从第一个进入
			}
			else if (x==door.x2&&(t.dir == 2 && y + 3 == door.y2 || t.dir == 0 && y == door.y2)) {
				return 2;//从第二个进入
			}
		}
	}
	return 0;
}//检查关于传送门
```



> 判定机制搞清楚了，那么接下来就是实现。实现大致分为两步，生成传送门和传送坦克。

##  生成传送门

传送门过一段时间刷新。生成传送门可不是简单的随机数就好了，你总不能把传送门放在坦克身上吧，也不能放在铁块里面吧，所以**传送门所要占据的空间在生成的这一个瞬间应该全部为空**，因此，在使用随机数生成之后还需要查看位置是否合法才可以退出。

同时，生成传送门的时候你还需要定下来传送点，尽管在实际写的时候我是后来才想到要先定下来的。因为传送出来不能卡在墙里，或者传送到其他不合法位置，下面就传送的传出位置做两种情况下的讨论：

**第一种**是坦克从哪里进来，然后你传送也从哪个方向出去，其实这个是最理想的方案，但是対生成的要求就提高了，也就是如果你生成一个横着的传送门，坦克传送门的上面走下来，就会从另一个传送门的下面走出去，同时考虑各种情况，**也就是这个传送门的上下一个坦克位置都要全部为空，这尤其在游戏初期方块众多的情况下是难以满足的**，随机数的寻找并没有一定规律，所以个人觉得容易导致程序崩溃，所以采取了另外一种方案。

 **第二种**就是进入点不管，但是传出点固定，你可以从上面下面进来，但是你被传送只能到另一个传送门吧的上面（或者下面），这样的好处就是**每个传送门只需要有一端是可以被传送到的，那就是合法的，到时候传送也直接传送到这个地方，对于判定的条件限制相对就没那么严格**。你只需要在生成传送门的时候就去检查，一边可不可以，可以就记录坐标，不可以换另一边，可以记录坐标，不可以就直接把这个传送门坐标咔嚓掉，因为两边都不是合法传送点。

试验之后效果还好，没有出现程序崩溃情况，第一种没试过。

```cpp
struct Door {
	int num;
	int x1,y1,x2,y2;//两个传送门的坐标
	int arrx1, arrx2, arry1, arry2;//两个传送门对应的到达坐标
	int dir1, dir2;//出传送门的方向，可额能没用到?
	int type;//传送门类型，上下 0/左右 1
	int flag[NUMY + 2][NUMX + 2];//存储信息
}door;//关于传送门的结构体

/*更新传送门*/
void UpDataDoor() {
	if (door.x1 >= 0) {//清除原有的
		if (door.type) {//左右
			clearrectangle(door.x1*SIZE, door.y1*SIZE, door.x1*SIZE + 4 * SIZE - 1, door.y1*SIZE + SIZE - 1);
			clearrectangle(door.x2*SIZE, door.y2*SIZE, door.x2*SIZE + 4 * SIZE - 1, door.y2*SIZE + SIZE - 1);
		}
		else {//上下
			clearrectangle(door.x1*SIZE, door.y1*SIZE, door.x1*SIZE + SIZE - 1, door.y1*SIZE + 4 * SIZE - 1);
			clearrectangle(door.x2*SIZE, door.y2*SIZE, door.x2*SIZE + SIZE - 1, door.y2*SIZE + 4 * SIZE - 1);
		}
		for (int i = 0; i <= 4; i++) {
			if (door.type) {
				door.flag[door.y1][door.x1 + i] = 0;
				door.flag[door.y2][door.x2 + i] = 0;
			}
			else {
				door.flag[door.y1 + i][door.x1] = 0;
				door.flag[door.y2 + i][door.x2] = 0;
			}
		}
	}
	//生成传送门
	door.type = MyRand() % 2;
	//door.type = 1;
	int num = 0;//当前找到的个数
	int x, y;//随机生成的一组坐标
	while (num < 2) {
		srand(rand_seed);//重置时间种子
		rand_seed = rand();//随机种子链
		x = rand() % (NUMX - 10)+ 6;
		y = rand() % (NUMY - 8 )+ 6;//左上角单位坐标
		//必须是空地(没有任何东西)
		bool if_ok = 1;//表示是否可以
		for (int i = 0; i <= 4; i++) {//检查四个点
			if (door.type) {//检查一排
				if (mat[y][x + i] || flag[y][x + i] != -1 || flag[y + 1][x + i] != -1 || num && abs(x - door.x1) + abs(y - door.y1) <40) {
					if_ok = 0;
					break;
				}
			}
			else {//检查一列
				if (mat[y+i][x ] || flag[y+i][x]!=-1 || flag[y + i][x + 1]!=-1 || num && abs(x - door.x1) + abs(y - door.y1) < 40) {
					if_ok = 0;
					break;
				}
			}
		}
		//接下来查找坦克的合法传送位置
		if (door.type) {//左右类型传送门传上下
			//先看上面有没有位置
			if (CheckIfCanExist(x,y-4,x+3,y-1)) {//表明上面可以传送
				if (num) {
					door.arrx2 = x;
					door.arry2 = y - 4;
					door.dir2 = 0;
				}
				else{
					door.arrx1 = x;
					door.arry1 = y - 4;
					door.dir1 = 0;
				}
			}
			else {//否则找下面
				if (CheckIfCanExist(x,y+1,x+3,y+4)) {
					if (num) {
						door.arrx2 = x;
						door.arry2 = y + 1;
						door.dir2 = 2;
					}
					else {
						door.arrx1 = x;
						door.arry1 = y + 1;
						door.dir1 = 2;
					}
				}
				else {
					if_ok = 0;//两边都找过还不可以
				}
			}
		}
		else {
			//先看左边有没有位置
			if (CheckIfCanExist(x-4, y, x-1, y+3)) {//表明左边可以传送
				if (num) {
					door.arrx2 = x - 4;
					door.arry2 = y;
					door.dir2 = 3;
				}
				else {
					door.arrx1 = x - 4;
					door.arry1 = y;
					door.dir1 = 3;
				}
			}
			else {//否则找右边
				if (CheckIfCanExist(x+1, y , x + 4, y +3)) {
					if (num) {
						door.arrx2 = x + 1;
						door.arry2 = y;
						door.dir2 = 1;
					}
					else {
						door.arrx1 = x + 1;
						door.arry1 = y;
						door.dir1 = 1;
					}
				}
				else {
					if_ok = 0;//两边都找过还不可以
				}
			}
		}
		if (!if_ok) {
			continue;//不行
		}
		//更新标记点
		for (int i = 0; i < 4; i++) {
			if (door.type) {
				door.flag[y][x + i] = 1;
			}
			else {
				door.flag[y + i][x] = 1;
			}
		}
		num++;
		putimage(x*SIZE + 1, y*SIZE + 1, &img[30+door.type]);
		if (num == 1) {
			door.x1 = x;
			door.y1 = y;
		}
		else {
			door.x2 = x;
			door.y2 = y;
		}
	}
}//更新传送门
```



## 传送坦克

其实传送点坐标定下来之后，传送模块的实现已经呼之欲出了 ，那就是只要符合进入条件，直接把原来坦克清除，然后下一步坐标定位到传送点坐标。

因为传送一定是在按下上下左右之后实现的，所以就把这个触发点放在了上下左右键的判定里面。

只要进入传送门，会一定可以到另外一个出口的，因为前面已经判定过，小问题就是可能会到其他坦克身上，因为这个是动态的，但是总不能不让传送，也无伤大雅吧。

代码如下：

```cpp
/*四个方向*/
void control_dir(int now_dir) {
	if (tank[0].dir != now_dir) //原来不是在这个
	{
		tank[0].dir = now_dir;
		draw_a_tank(tank[0], tank[0].x, tank[0].y);
		tank[0].bef_move = now_dir;//更新
	}
	else if (tank[0].bef_move >= type[tank[0].type].Speed)//可以移动
	{
		int xx = tank[0].x + dirx[tank[0].dir] * SIZE;
		int yy = tank[0].y + diry[tank[0].dir] * SIZE;//坐标
		int check_door = CheckDoor(tank[0],xx/SIZE,yy/SIZE);//检查关于传送门,根据返回值来看位置
		//更新位置，关于传送门
		if (check_door) {
			if (check_door == 1) {
				xx = door.arrx2*SIZE;
				yy = door.arry2*SIZE;
			}
			else {
				xx = door.arrx1*SIZE;
				yy = door.arry1*SIZE;
			}
		}
		int check = check_wz(xx, yy, 0);
		if (check_door||check) { //检查位置
			draw_a_tank(tank[0], xx, yy);//xx,yy表示更新后的位置
			if (check_door) {//如果是走进了传送门
				Sleep(200);
				mark_tank(tank[0].x, tank[0].y, -1);
				mark_tank(xx, yy, 0);
			}
			else {
				update_mark(tank[0].dir, xx, yy, 0);//普通移动更新坦克位置信息
			}
			tank[0].x = xx;
			tank[0].y = yy;
			tank[0].bef_move = 0;//更新时间
			if (check != 10) {
				check_tool(check);//检查道具
				tool.exist = 0;
			}
		}
	}
}//四个方向
```



#  关卡模式

> 传统的坦克大战的一关一关闯关其实还是比较有意思的，所以我也模仿着简单设计了一下关卡。

为了扩展方便，我把每一关的信息放在了数组里面，然后就是在游戏主控部分加了一个循环，循环的下标实际上就意味着一个个关卡，每次读取信息然后按照信息生成关卡。关于一些过渡的动画动画，我就去抄复制了 我之前写的连连看的，然后衔接也还能看的过去。

![img](https://s21.ax1x.com/2025/11/26/pZAqijI.png)

 修改后的游戏主函数代码如下，其中提到的数组在**完整代码**的最上面：

需要说明一下的就是get_map()函数其实就是从一个三维数组（全部地图）里面读一个二维数组(一张地图)放到map中，可能写的有点啰嗦，也是各种报错之后的不得已之举。

```cpp
void game() {
	initgraph(MAXX + 1 + 400, MAXY + 1, SHOWCONSOLE);
	ini_all();//全局初始化函数,一次游戏只初始化一次(区别于关卡初始化)

	for (int i = 1; i <= 2;i++)//关卡数字循环,当前是i+1关
	{
		now_level = i;
		printNextLevel();//打印转场动画
		while (1) {
			Sleep(10);
			settextcolor(WHITE);
			settextstyle(400, 40, 0);
			outtextxy(300, 300, "点击ENTER开始游戏...");
			if (KEY_DOWN(13)) {//点击enter进入
				cleardevice();
				int num = get_map(map,all_map[now_level-1]);//将当前的关卡地图从all_map中写到map中.并且返回数量
				ini_every(map, num);//关卡初始化
				ini_tank();//初始化出坦克
				draw_all_tank();//画坦克
				begin();//游戏开始,主控程序
				while (1) {
					Sleep(10);
					if (KEY_DOWN(VK_ESCAPE)) {//读取玩家开始信息
						cleardevice();
						break;
					}
				}
				break;
			}
		}
	}
}

```



# 效果展示




![img](https://i.cetsteam.com/imgs/2025/11/27/2101d90160b99408.png)

![img](https://i.cetsteam.com/imgs/2025/11/27/dacb216bc7986abc.png)

![img](https://i.cetsteam.com/imgs/2025/11/27/aa1d9bcf584e5171.png)

![img](https://i.cetsteam.com/imgs/2025/11/27/f1cbfd9494135705.png)
# 总结

差不多到这里就完成了功能的开发，玩起来还是不错的。实际上还可以做比较多东西，更新一些模式，就先做这么多吧。

总的还是感觉后面功能多起来之后一个文件其实还是挺乱的（最后已经一千五百行了...），所以还是要考虑用类的方式来写，可能也会整齐很多，不然很多函数都要找半天，扩展起来就会麻烦不少。

# **完整代码**

main.cpp

```cpp
#include"head.h"

int main() {
	game();
	return 0;
}

```



head.h

```cpp
#define _CRT_SECURE_NO_WARNINGS 1
#include<stdio.h>
#include<algorithm>
#include <stdlib.h>
#include <time.h>
#include<graphics.h>//图形绘制库
#include<string>
#include<iostream>
#include<queue>
#include<conio.h>
#include<vector>
#include<math.h>
#include<stack>
using namespace std;

#define SIZE 15 //定义最小单元
#define NUMX 64
#define NUMY 56//定义xy轴的最小单元个数
#define MAXX NUMX*SIZE
#define MAXY NUMY*SIZE
#define PI 3.1415926
#define RIGHT 77
#define LEFT 75
#define UP 72
#define DOWN 80
#define ENTER 13
#define W 119
#define A 97
#define S 115
#define D 100
#define KONG 32
#define ESC 27//获取键值
#define KEY_DOWN(vKey) ((GetAsyncKeyState(vKey) & 0x8000) ? 1:0)//检测键盘按下函数

void game();//游戏函数入口
void PrintTankNum();

```



tool.cpp

```cpp
#include"head.h"

int dirx[] = {0,1,0,-1};
int diry[] = {-1,0,1,0};//四个方向
int img_zidan_detx1[] = {-SIZE/2,-SIZE,-SIZE/2,0};
int img_zidan_dety1[] = {0,-SIZE/2,-SIZE,-SIZE/2};//子弹左上角相对弹头偏移量,画子弹要用
int check_if_movex[][4] = { {-1,-1,-1,0},{0,-1,0,0} };
int check_if_movey[][4] = { {0,-1,-1,-1 },{0,0,-1,0} };//表示子弹合法检查位置相对弹头偏移单位数，相邻两个都检查
int all_tank_num = 3;//其他类型坦克数量
int rest_num;//剩余坦克数量
int my_lives;//自己的坦克数量
int now_level;//当前关卡
int rand_seed;//随机种子
int level;//自身当前等级
int mat[NUMY+2][NUMX+2];//存储游戏地图
int flag[NUMY + 2][NUMX + 2];//存储坦克标记
int stop_epoch;//表示停止回合,道具使用
int tool_speed;//道具更新速率
int door_speed;//传送门更新速率
//123分别存储三种障碍物，(-10)存储自己坦克所在位置,(-1)-(-9)存储敌方坦克位置，一个坦克占据16个单元格，左上坐标标记
int num_tank;//当前场上的坦克
int birth_wz[][2] = { {MAXX / 2 - SIZE * 12,MAXY - 4 * SIZE},			//第一个是自己的出生点，剩下的是其他坦克出生点
					{0,0},{MAXX / 2 - 16 * SIZE,0},{MAXX / 2 + 16 * SIZE,0},{MAXX - 4 * SIZE,0} };
int rep_map[][5] = { {2,6,12,7,14},{2,6,11,10,12},{2,9,12,10,14} };//修复老家的元素
// 配置关卡
// 1表示铁块，2表示红砖，3表示草地，后面是x1 y1 x2 y2 是在最小单元为2*SIZE的地图上的每个类型方块的左上角和右下角坐标
int map[100][5];//存储每一关的地图
int all_map[3][70][5] =  //所有地图
{
	{
		{2,0,2,2,8} ,{2,4,2,6,3},{1,7,0,9,2},{1,12,2,14,4},
		{3,4,4,6,6},{2,6,4,14,6},{3,14,4,16,6},{1,2,6,4,10},
		{2,1,10,3,11},{3,4,7,5,8},{1,5,7,7,9},{2,7,7,11,9},
		{1,11,7,13,9},{2,13,7,15,14},{2,1,11,3,14},{1,4,3,6,4},
		{2,6,12,7,14},{2,6,11,10,12},{2,9,12,10,14}
	},
	{
		{1,2,2,4,4},{1,7,0,9,1},{1,12,2,13,4},{1,5,3,7,4},{1,10,3,11,4},{1,6,6,7,8},
		{1,9,6,10,8},{1,12,6,13,8},{1,7,7,8,10},{1,9,9,10,10},{1,13,5,15,6},{1,1,2,2,4},
		{2,5,1,11,3},{2,7,3,9,5},{2,13,2,15,4},{2,2,6,5,8},{2,3,5,5,6},{2,7,6,9,8},{2,13,6,15,9},
		{2,2,10,3,14},{2,3,9,6,10},{2,12,9,12,10},{2,12,12,13,14},{2,14,10,15,14},
		{2,1,10,2,14},{2,1,6,2,8},{2,10,6,12,8},{2,0,6,1,7},{2,15,6,16,7},
		{3,0,2,1,4},{3,0,7,1,8},{3,0,10,1,14},{3,4,2,5,4},{3,11,2,12,4},{3,7,5,7,6},{3,15,2,16,4},
		{3,15,7,16,8},{3,15,10,16,14},{3,0,8,1,10},{3,15,8,16,10},
		{2,6,12,7,14},{2,6,11,10,12},{2,9,12,10,14}
	},
	{
		{2,0,4,16,10},
		{2,6,12,7,14},{2,6,11,10,12},{2,9,12,10,14}
	}
};
int all_other_tank[] = {20,25,30};//存储每一关的敌方坦克数量
int tool_update_speed[] = { 1000,1000,400 };//道具更新速率
int door_update_speed[] = { 1500,1500,500 };//传送门更新速率

struct Zidan {
	int who;//记录的是在坦克系列，1-3是自己，其他是其他人
	int dir;//方向
	int x;
	int y;//坐标
	bool count;//为了标记是否是第一次被更新
};
queue<Zidan>qz;

struct Tool {
	int num;//更新计时器
	bool exist;//是否存在
	int flag[NUMY + 2][NUMX + 2];//存储道具信息
}tool;

struct Door {
	int num;
	int x1, y1, x2, y2;//两个传送门的坐标
	int arrx1, arrx2, arry1, arry2;//两个传送门对应的到达坐标
	int dir1, dir2;//出传送门的方向，可额能没用到?
	int type;//传送门类型，上下 0/左右 1
	int flag[NUMY + 2][NUMX + 2];//存储信息
}door;//关于传送门的结构体

struct Tank {
	int type;//类型
	int HP;//血量
	int dir;//方向,上下左右0123
	int x, y;//坐标
	int bef_fight;//上一次攻击到现在的间隔
	int bef_move;//上一次移动到现在的间隔
	int stop_time;//表示该坦克在几个周期内不移动
	int dir_stop_time;//表示该坦克在几个周期内不改变方向
	int move_type;//1代表优先向中间，2代表优先向下面
	int state;//当前状态
}tank[10];//最多其实就4个,0号始终是自己

// 不同坦克类型属性
struct Type {
	/*自身属性*/
	int HP;//血量
	int Speed;//速度,单位是更新周期,一个更新周期子弹移动一次
	int Power;//伤害
	int Fight_det;//攻击间隔,单位是更新周期
}type[10];
IMAGE img[40];//存储图片

/*调试*/
void test() {
	clearrectangle(0, 0, 500, 500);
}//调试

/*程序停止*/
void stop() {
	system("pause");
}//程序停止

/*生成随机数*/
int MyRand() {
	srand(rand_seed);
	int t = rand();
	rand_seed = rand();//重置
	return t;
}//生成随机数

/*读入和写入地图*/
int get_map(int map[][5],int all_map[][5])
{
	//返回数量
	int num = 0;
	for (int i = 0; i < 70; i++) {
		if (all_map[i][0] == 0) {//代表是最后一个
			return num;
		}
		else {//不是空
			for (int j = 0; j < 5; j++) {
				map[i][j] = all_map[i][j];
			}
			num++;
		}
	}
}//读入和写入地图

/*全局初始化*/
void ini_all() {
	/*自身属性状态*/
	//一级
	type[1].HP = 1;
	type[1].Speed = 8;
	type[1].Power = 10;
	type[1].Fight_det = 30;
	//二级
	type[2].HP = 1;
	type[2].Speed = 7;
	type[2].Power = 15;
	type[2].Fight_det = 25;
	//三级
	type[3].HP = 2;
	type[3].Speed = 6;
	type[3].Power = 20;
	type[3].Fight_det = 20;
	/*其他坦克状态*/
	//第一种血量高的
	type[4].HP = 2;
	type[4].Speed = 10;
	type[4].Power = 10;
	type[4].Fight_det = 40;
	//第二种速度快的
	type[5].HP = 1;
	type[5].Speed = 5;
	type[5].Power = 10;
	type[5].Fight_det = 30;
	//第三种攻击快
	type[6].HP = 1;
	type[6].Speed = 8;
	type[6].Power = 10;
	type[6].Fight_det = 20;
	//第四种是的一种被打了之后的样子
	type[7].HP = 1;
	type[7].Speed = 10;
	type[7].Power = 10;
	type[7].Fight_det = 40;
	/*图片*/
	loadimage(&img[1], "./imgs/type1_1.png");
	loadimage(&img[2], "./imgs/type1_2.png");
	loadimage(&img[3], "./imgs/type2_1.png");
	loadimage(&img[4], "./imgs/type2_2.png");
	loadimage(&img[5], "./imgs/type3_1.png");
	loadimage(&img[6], "./imgs/type3_2.png");
	loadimage(&img[7], "./imgs/type4_1.png");
	loadimage(&img[8], "./imgs/type4_2.png");
	loadimage(&img[9], "./imgs/type5_1.png");
	loadimage(&img[10], "./imgs/type5_2.png");
	loadimage(&img[11], "./imgs/type6_1.png");
	loadimage(&img[12], "./imgs/type6_2.png");
	loadimage(&img[13], "./imgs/type7_1.png");
	loadimage(&img[14], "./imgs/type7_2.png");
	loadimage(&img[15], "./imgs/铁块.png");//铁块
	loadimage(&img[16], "./imgs/红砖.png");//红砖
	loadimage(&img[17], "./imgs/草丛_1.png");//草地
	loadimage(&img[18], "./imgs/草丛_2.png");//草地
	loadimage(&img[19], "./imgs/家.png");//家
	loadimage(&img[20], "./imgs/敌房子弹_1.png");
	loadimage(&img[21], "./imgs/敌房子弹_2.png");
	loadimage(&img[22], "./imgs/我房子弹_1.png");
	loadimage(&img[23], "./imgs/我房子弹_2.png");//子弹
	loadimage(&img[24], "./imgs/家爆炸.png");//家被炸掉了
	loadimage(&img[25], "./imgs/坦克爆炸.png");//坦克被炸掉了
	loadimage(&img[26], "./imgs/炸弹.png");//随机炸掉一个坦克
	loadimage(&img[27], "./imgs/停止.png");//其他坦克停止操作几个周期
	loadimage(&img[28], "./imgs/升级.png");//坦克升级
	loadimage(&img[29], "./imgs/修复.png");//修复老家
	loadimage(&img[30], "./imgs/传送门_上下.png");//传送门类型1
	loadimage(&img[31], "./imgs/传送门_左右.png");//传送门类型2
}//全局初始化

/*关卡初始化*/
void ini_every(int mp[][5], int num) {
	door.num = 1;//一开始就刷新传送门
	door.x1 = -1;
	door.x2 = -1;
	door.y1 = -1;
	door.y2 = -1;
	tool.num = 0;
	tool.exist = 0;
	tool_speed = tool_update_speed[now_level];
	door_speed = door_update_speed[now_level];
	rest_num = all_other_tank[now_level];//敌方坦克数量
	my_lives = 3;
	level = 1;//一级
	putimage(MAXX + 100, 100, &img[2]);
	putimage(MAXX + 100, 200, &img[8]);//计数榜里面的坦克
	PrintTankNum();
	srand((unsigned)time(NULL));//调整种子
	rand_seed = rand();
	line(MAXX + 1, 0, MAXX + 1, MAXY + 1);//分割线
	putimage(7 * SIZE * 4, 12 * SIZE * 4, &img[19]);
	for (int i = 0; i <= NUMY; i++){
		for (int j = 0; j <= NUMX; j++) {
			mat[i][j] = 0;
			flag[i][j]= -1;//先初始化为-1
			tool.flag[i][j] = 0;
			door.flag[i][j] = 0;
		}
	}
	for (int i = 12 * 4; i <= 12 * 4 + 8; i++) {
		for (int j = 7 * 4; j <= 7 * 4 + 8; j++) {
			flag[i][j] = 10;//标记为老家位置
		}
	}
	for (int i = 0; i < num; i++) {
		int k = mp[i][0];
		int x1 = mp[i][1]*4;
		int y1 = mp[i][2]*4;
		int x2 = mp[i][3]*4-1;
		int y2 = mp[i][4]*4-1;//右下角方块的左上角坐标
		for (int xx = x1; xx <= x2; xx++) {
			for (int yy = y1; yy <= y2; yy++) {
				mat[yy][xx] = k;
			}
		}
		if (k == 2) {//泥土块最小单位SIZE
			for (int xx = x1; xx <= x2; xx++) {
				for (int yy = y1; yy <= y2; yy++) {
					putimage(xx*SIZE, yy*SIZE, &img[16]);
				}
			}
		}
		else {//铁块和草方块最小单位2*SIZE
			for (int xx = mp[i][1]*2; xx <= mp[i][3]*2-1; xx++) {
				for (int yy = mp[i][2]*2; yy <= mp[i][4]*2-1; yy++) {
					if (k == 1) {//铁块
						putimage(xx*SIZE * 2, yy*SIZE * 2, &img[15]);
					}
					else {//草地
						putimage(xx*SIZE * 2, yy*SIZE * 2, &img[17], SRCAND);
						putimage(xx*SIZE * 2, yy*SIZE * 2, &img[18], SRCPAINT);
					}
				}
			}
		}
	}
}//关卡初始化

/*转换数字为字符串*/
void deal(int x, char *a) {
	stack <int>sst;
	while (x) {
		sst.push(x % 10);
		x /= 10;
	}
	int l = 0;
	while (!sst.empty()) {
		a[l++] = sst.top() + ''0'';
		sst.pop();
	}
	a[l] = ''\0'';
	if (a[0] == ''\0'') {
		a[0] = ''0'';
		a[1] = ''\0'';
	}
}//转换数字为字符串

/*打印游戏结束*/
void PrintGameOver() {
	settextcolor(WHITE);
	int size = 100;//字体大小
	settextstyle(size, size, 0);
	outtextxy(90,200,"游戏结束");
}//打印游戏结束

/*打印关卡过渡动画*/
void printNextLevel() {
	cleardevice();
	char a[100];
	char info[100] = "第";//拼接数组
	//打印时间耗尽
	settextcolor(BROWN);
	int size =100;//字体大小
	settextstyle(size, size, 0);
	deal(now_level+1, a);
	strcat(info, a);
	strcat(info, "关");
	outtextxy(MAXX / 2 + 200 - size * 2, MAXY / 2 - size, info);
	Sleep(1000);
	cleardevice();
}//打印关卡过渡动画

/*通关*/
void PrintSucess() {
	settextcolor(WHITE);
	int size = 100;//字体大小
	settextstyle(size, size, 0);
	outtextxy(90, 200, "成功通关");
}//通关

/*打印坦克数量*/
void PrintTankNum() {
	char a[100];

	//计算自己坦克数量
	settextcolor(WHITE);
	settextstyle(40, 40, 0);
	outtextxy(MAXX+100+4*SIZE, 120, "X");
	deal(my_lives, a);
	//这是为了防止倒计时显示错误
	clearrectangle(MAXX + 100 + 4 * SIZE + 50, 120, MAXX + 100 + 4 * SIZE + 50 + 70, 120 + 50);
	outtextxy(MAXX + 100 + 4*SIZE+50, 120, a);

	//计算敌方坦克数量
	settextcolor(WHITE);
	settextstyle(40, 40, 0);
	outtextxy(MAXX + 100 + 4 * SIZE, 220, "X");
	deal(rest_num, a);
	clearrectangle(MAXX + 100 + 4 * SIZE + 50, 220, MAXX + 100 + 4 * SIZE + 50 + 70, 220 + 50);
	outtextxy(MAXX + 100 + 4 * SIZE + 50, 220, a);
}//打印坦克数量

/*标记坦克占据空间*/
void mark_tank(int x,int y,int mark_num) {
	for (int i = 1; i <= 3; i++) {
		for (int j = 1; j <= 3; j++) {
			flag[y / SIZE + i][x / SIZE + j] = mark_num;
		}
	}
}//标记坦克占据空间

/*更新坦克标记点*/
void update_mark(int dir, int x, int y,int index) {//新的方向和新的坐标定位点
	switch (dir) {//四种方向，上右下左
		//坦克标记点有5个
	case 0:
		for (int i = 1; i < 4; i++) {
			flag[y / SIZE + 1][x / SIZE + i] = index;
			flag[y / SIZE + 4][x / SIZE + i] = -1;//清空后面的
		}
		break;
	case 1:
		for (int i = 1; i < 4; i++) {
			flag[y / SIZE + i][x / SIZE + 3] = index;
			flag[y / SIZE + i][x / SIZE ] = -1;//清空后面的
		}
		break;
	case 2:
		for (int i = 1; i < 4; i++) {
			flag[y / SIZE ][x / SIZE + i] = -1;
			flag[y / SIZE + 3][x / SIZE + i] = index;
		}
		break;
	case 3:
		for (int i = 1; i < 4; i++) {
			flag[y / SIZE + i][x / SIZE+1] = index;
			flag[y / SIZE + i][x / SIZE + 4] = -1;//清空后面的
		}
		break;
	}
}//更新坦克标记点

/*产生一个坦克*/
Tank add_tank(int add_place,int wz) {
	//add_place表示产生位置，取值1234表示敌方坦克的出生位置，wz表示在坦克数组中的位置
	Tank t;
	t.dir = 3;
	t.bef_fight = 0;
	t.bef_move = 0;
	t.stop_time = rand()%5;
	t.dir_stop_time = 0;
	if (add_place) {//敌方坦克
		srand(rand_seed);//重置时间种子
		rand_seed = rand();//随机种子链
		int index = rand() % all_tank_num + 4;
		t.move_type = rand() % 2 + 1;
		t.type = index;
		t.HP = type[index].HP;
		t.x = birth_wz[add_place][0];
		t.y = birth_wz[add_place][1];
		t.state = 1;//表示活着
		mark_tank(t.x, t.y, wz);
	}
	else {//自己
		t.HP = type[level].HP;
		t.type = level;//通过type索引可以得到速度等信息
		t.x = birth_wz[0][0];
		t.y = birth_wz[0][1];
		mark_tank(t.x, t.y, 0);//0号标记是自己
	}
	return t;
}//产生一个坦克

/*初始化坦克队列*/
void ini_tank() {
	num_tank = 0;
	tank[0]=add_tank(0,0);//0表示产生己方坦克，其他表示其他坦克，编号index
	//在1 2 3号出生点设置敌方坦克
	for (int i = 1; i <= 3; i++) {
		num_tank++;
		tank[i]=add_tank(i, num_tank);
	}
}//初始化坦克队列

/*检查是否穿过草丛*/
bool CheckGrass(int xx,int yy) {
	for (int i = 0; i < 4; i++) {
		for (int j = 0; j < 4; j++) {
			if (mat[yy / SIZE + i][xx / SIZE + j] == 3) {
				return 1;//有穿过草地
			}
		}
	}
	return 0;
}//检查是否穿过草丛

/*修复草地---坦克经过草地*/
void add_grass(int xx, int yy) {
	bool tmp_map[4][4];
	int detx = 0, dety = 0;//左上角
	int last_x, last_y;//右下角
	bool if_first = 0;//标记是否是第一次出现，是为了标记左上角坐标
	for (int i = 0; i < 4; i++) {
		for (int j = 0; j < 4; j++) {
			tmp_map[i][j] = (mat[yy / SIZE + i][xx / SIZE + j] == 3);//有坦克并且经过
			if (!if_first&&mat[yy / SIZE + i][xx / SIZE + j] == 3) {
				detx = j;
				dety = i;
				if_first = 1;
			}
			if (tmp_map[i][j]) {
				last_x = j;
				last_y = i;
			}
		}
	}
	detx -= (xx / SIZE + detx) % 2;
	dety -= (yy / SIZE + dety) % 2;
	for (int i = dety; i <= last_y; i += 2) {
		for (int j = detx; j <= last_x; j += 2) {
			if (i < 0&&tmp_map[0][j] || j < 0&&tmp_map[i][0] || tmp_map[i][j]) {//需要放上图片
				putimage(xx + j * SIZE, yy + i * SIZE, &img[17], SRCAND);
				putimage(xx + j * SIZE, yy + i * SIZE, &img[18], SRCPAINT);
			}
		}
	}
}//修复草地---坦克经过草地

/*画一个坦克*/
void draw_a_tank(Tank t,int xx,int yy) {
	IMAGE ans1,ans2;
	clearrectangle(t.x+3, t.y+3, t.x + 4 * SIZE-1, t.y + 4 * SIZE-1);
	rotateimage(&ans1, &img[t.type*2-1], -PI / 2 * t.dir);//顺时针旋转dir个90度,然后赋值给ans
	rotateimage(&ans2, &img[t.type*2], -PI / 2 * t.dir);
	putimage(xx + 4, yy + 4, &ans1, SRCAND);
	putimage(xx + 4, yy + 4, &ans2, SRCPAINT);
	if (CheckGrass(t.x,t.y)) {//如果草丛
		add_grass(t.x, t.y);
	}
}//画一个坦克

/*画全部坦克*/
void draw_all_tank() {
	for (int i = 0; i <= num_tank; i++) {
		draw_a_tank(tank[i],tank[i].x,tank[i].y);
	}
}//画全部坦克

/*表示从旧的变成新的*/
void up_date_type(int new_type, int wz) {
	tank[wz].type = new_type;
	tank[wz].HP = type[new_type].HP;
	draw_a_tank(tank[wz], tank[wz].x, tank[wz].y);
}//表示从旧的变成新的

/*检查坦克位置合法性*/
int check_wz(int xx, int yy, int wz) {
	//检查是否出界
	if (xx<0 || yy<0 || xx + 4 * SIZE>MAXX || yy + 4 * SIZE>MAXY||wz&&tool.flag[yy/SIZE][xx/SIZE]) {
		return 0;
	}
	//检查坦克所在位置是否有障碍物以及道具
	int tool_x = -1, tool_y = -1;
	for (int i = 0; i < 4; i++) {
		for (int j = 0; j < 4; j++) {
			//泥土和铁块不能跨过，其他坦克不能穿越道具,不能踩到传送门
			if (mat[yy / SIZE + i][xx / SIZE + j] == 1 || mat[yy / SIZE + i][xx / SIZE + j] == 2
			|| flag[yy / SIZE + i][xx / SIZE + j] >= 0 && flag[yy / SIZE + i][xx / SIZE + j] != wz || door.flag[yy / SIZE + i][xx / SIZE + j])
			{
				//已经有障碍物或者有其他坦克
				return 0;
			}
			if (tool.flag[yy / SIZE + i][xx / SIZE + j]) {
				if (wz)return 0;
				else {
					tool_x = xx / SIZE + j;
					tool_y = yy / SIZE + i;
				}
			}
		}
	}
	//检查无障碍物并且有道具
	int index = tool.flag[tool_y][tool_x];
	if (tool_x != -1) {
		//清空标记
		if (tool_x > 0 && tool.flag[tool_y][tool_x - 1])
			tool_x--;
		if (tool_y > 0 && tool.flag[tool_y - 1][tool_x])
			tool_y--;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				tool.flag[tool_y + i][tool_x + j] = 0;
			}
		}
		clearrectangle(tool_x*SIZE, tool_y*SIZE, tool_x*SIZE + 2 * SIZE, tool_y*SIZE + 2 * SIZE);
		draw_a_tank(tank[0], tank[0].x, tank[0].y);//重画坦克
		return index;
	}
	return 10;
}//检查坦克位置合法性

/*清除坦克*/
int clear_tank(int x,int y,int type_of) {
	//test();
	int xx = x / SIZE;
	int yy = y / SIZE;//记录消除单位坐标
	int bh = flag[yy][xx];//坦克在坦克队列中的编号，0是自己
	if (type_of) {//表示是炸弹的魔法伤害
		tank[bh].HP = 0;
	}
	else {//否则就是普通子弹
		tank[bh].HP--;
	}
	if (tank[bh].HP) {
		if (bh) {
			up_date_type(7, bh);//两条命的敌人
		}
		else {
			up_date_type(level-1, bh);//自身
		}
		return 0;//表示还没挂掉,直接返回
	}
	//向左上角追溯
	while (flag[yy][xx] == bh) {
		xx--;
	}
	xx++;//为了接下来还可以查找y,因为找到的位置x实际上是没有flag标记的
	while (flag[yy][xx] == bh) {
		yy--;
	}
	yy++;//统一，这样左上角坐标就是[y-1][x-1]
	//清除坦克
	clearrectangle((xx-1)*SIZE, (yy-1)*SIZE, (xx-1)*SIZE + 4 * SIZE - 1, (yy-1)*SIZE + 4 * SIZE - 1);
	putimage((xx - 1)*SIZE + 2, (yy - 1)*SIZE + 2, &img[25]);
	Sleep(100);
	clearrectangle((xx - 1)*SIZE, (yy - 1)*SIZE, (xx - 1)*SIZE + 4 * SIZE - 1, (yy - 1)*SIZE + 4 * SIZE - 1);
	if (CheckGrass(tank[bh].x, tank[bh].y)) {//如果草丛
		add_grass(tank[bh].x, tank[bh].y);
	}
	//清空标记点
	for (int i = yy ; i <yy+ 3; i++) {
		for (int j = xx ; j < xx + 3; j++) {
			flag[i][j] = -1;
		}
	}
	if (bh == 0) {//代表自己被打死了
		level = 1;
		tank[0]=add_tank(0, 0);
		draw_a_tank(tank[0], tank[0].x, tank[0].y);
		my_lives--;
	}
	else {//其他坦克
		rest_num--;
		if (!rest_num)//坦克清空
		{
			return 1;//表示游戏结束(打完了)
		}
		else {
			if (rest_num < 3) {
				tank[bh].state = 0;//当前位置没有坦克
			}
			else {
				//产生一个新坦克
				srand(rand_seed);//重置时间种子
				rand_seed = rand();//随机种子链
				int rand_place;
				while (1) {
					srand(rand_seed);//重置时间种子
					rand_seed = rand();//随机种子链
					rand_place = rand() % 4 + 1;
					if (check_wz(birth_wz[rand_place][0], birth_wz[rand_place][1], bh))
						break;
				}
				tank[bh] = add_tank(rand_place, bh);
			}
		}
	}
	PrintTankNum();
	return 0;//游戏继续
}//清除坦克

/*消除该区域内的物体*/
void remove_any(int detx1,int dety1, int detx2, int dety2,Zidan d)
{
	int x = d.x;
	int y = d.y;
	//计算起始xy坐标
	int stx = d.x / SIZE;
	int sty = d.y / SIZE;
	//控制区间
	if (dety1 == dety2) //横向排列
	{
		//先判断有空的情况
		int index = 2;
		for (int i = -1; i <= 1; i++)//找到跳变点并退出
		{
			if (mat[sty + dety1][stx + i] != mat[sty + dety1][stx + i - 1])
			{
				//要保证不是跟空比较
				index = i;
				break;
			}
		}
		if (!(mat[sty + dety1][stx + index] * mat[sty + dety1][stx + index - 1])) {
			//有空
			if (mat[sty + dety1][stx + index] + mat[sty + dety1][stx + index - 1] == 3)//一边是草
			{
				return;
			}
			index++;
			while (mat[sty + dety1][stx + index] == mat[sty + dety1][stx + index - 1]&&index<2)
			{
				index++;
			}
		}
		if (index!=2) {//表明有变化,那么可能是铁+草，铁+泥土，草+泥土任意组合
			//逐个讨论
			if (mat[sty + dety1][stx + index] == 3 || mat[sty + dety1][stx + index - 1] == 3
				|| door.flag[sty + dety1][stx + index - 1]+ door.flag[sty + dety1][stx + index])//修正消除位置，草不能被打掉
			{
				if (mat[sty + dety1][stx + index-1] == 3||door.flag[sty + dety1][stx + index - 1])//代表左边是草或者传送门
				{
					detx1 = index;//修改左边界
				}
				else
				{
					detx2 = index-1;//修改右边界
				}
			}
			else //一般坦克打的，不能消除铁，要判掉
			{
				if (mat[sty + dety1][stx + index-1] == 1)//代表左边是铁
				{
					detx1 = index;//修改左边界
				}
				else
				{
					detx2 = index - 1;//修改右边界
				}
			}
		}
		else if(mat[sty + dety1][stx + detx1] == 3){
			return;//全是草不考虑
		}
	}
	else //纵向排列
	{
		int index = 2;
		for (int i = -1; i <= 1; i++)//找到跳变点并退出
		{
			if (mat[sty + i][stx + detx1] != mat[sty + i-1][stx + detx1])
			{
				index = i;
				break;
			}
		}
		if (!(mat[sty + index - 1][stx + detx1] * mat[sty + index][stx + detx1])) {
			//有空
			if (mat[sty + index][stx + detx1] + mat[sty + index - 1][stx + detx1] == 3) {//空+草
				return;
			}
			index++;
			while (mat[sty + index][stx + detx1] == mat[sty + index - 1][stx + detx1] && index < 2)
			{
				index++;
			}
		}
		if (index!=2) {//表明有变化,那么可能是铁+草，铁+泥土，草+泥土任意组合
			//逐个讨论
			//带草 3
			if (mat[sty + index -1][stx + detx1] == 3 || mat[sty + index][stx + detx1] == 3
				||door.flag[sty + index - 1][stx + detx1]+door.flag[sty + index][stx + detx1] )//修正消除位置，草不能被打掉
			{
				if (mat[sty + index-1][stx + detx1] == 3|| door.flag[sty + index - 1][stx + detx1])//代表上是草
				{
					dety1 = index;//修改上边界
				}
				else
				{
					dety2 = index - 1;//修改下边界
				}
			}
			else //一般坦克打的，不能消除铁，要判掉
			{
				if (mat[sty + index -1][stx + detx1] == 1)//代表上边是铁
				{
					dety1 = index;//修改上边界
				}
				else
				{
					dety2 = index - 1;//修改下边界
				}
			}
		}
		else if (mat[sty + dety1][stx + detx1] == 3) {
			return;//全是草不考虑
		}
	}
	if (d.who != 3 && mat[y / SIZE + dety1][x / SIZE + detx1] == 1) {//修正之后指向铁，是铁+草的情况，上面未修正完全
		return;//无法消除
	}
	//计算消除矩阵坐标
	int x1 = x + detx1*SIZE;
	int y1 = y + dety1*SIZE;
	int x2 = x + detx2*SIZE+SIZE;
	int y2 = y + dety2*SIZE+SIZE;
	clearrectangle(x1, y1, x2-1, y2-1);
	for (int i = x / SIZE + detx1; i <= x / SIZE + detx2; i++)
	{
		for (int j = y / SIZE + dety1; j <= y / SIZE + dety2; j++) {
			mat[j][i] = 0;//清除标记
		}
	}
}//消除该区域内的物体

/*画一个子弹*/
void draw_zidan(Zidan d, int xx, int yy) {
	IMAGE ans1,ans2;
	rotateimage(&ans1, &img[((d.who <= 3) + 10) * 2], -PI / 2 * d.dir);//顺时针旋转dir个90度,然后赋值给ans
	rotateimage(&ans2, &img[((d.who <= 3) + 10) * 2+1], -PI / 2 * d.dir);//顺时针旋转dir个90度,然后赋值给ans
	putimage(xx + img_zidan_detx1[d.dir] + 1, yy + img_zidan_dety1[d.dir] + 1,&ans1, SRCAND);
	putimage(xx + img_zidan_detx1[d.dir] + 1, yy + img_zidan_dety1[d.dir] + 1,&ans2 , SRCPAINT);
}//画一个子弹

/*检查修补子弹打掉的草*/
void check_add_zidan_grass(Zidan d) {
	//推算左上角坐标并转化为单位形式
	int left_up_x = (d.x + img_zidan_detx1[d.dir]) / SIZE;
	int left_up_y = (d.y + img_zidan_dety1[d.dir]) / SIZE;
	left_up_x -= left_up_x % 2;
	left_up_y -= left_up_y % 2;//转换为草方块的定位点位置
	if (!(d.dir % 2)) {//上下
		int if_ou = 1-(d.x) / SIZE % 2;//如果子弹对称轴位于偶数线，那么就需要补充两张草
		//先判断要不要补充
		if (mat[left_up_y][left_up_x] == 3)//草
		{
			putimage(left_up_x*	SIZE, left_up_y*SIZE,&img[17], SRCAND);
			putimage(left_up_x*	SIZE, left_up_y*SIZE,&img[18], SRCPAINT);
		}
		if (if_ou&&mat[left_up_y][left_up_x + 2]==3)//看右边
		{
			putimage((left_up_x + 2) * SIZE, left_up_y * SIZE, &img[17], SRCAND);
			putimage((left_up_x + 2) * SIZE, left_up_y * SIZE, &img[18], SRCPAINT);
		}
	}
	else {//左右
		int if_ou = 1 - (d.y) / SIZE % 2;//如果子弹对称轴位于偶数线，那么就需要补充两张草
		//先判断要不要补充
		if (mat[left_up_y][left_up_x] == 3)//草
		{
			putimage(left_up_x*	SIZE, left_up_y*SIZE, &img[17], SRCAND);
			putimage(left_up_x*	SIZE, left_up_y*SIZE, &img[18], SRCPAINT);
		}
		if (if_ou&&mat[left_up_y+2][left_up_x] == 3)//看右边
		{
			putimage(left_up_x  * SIZE, (left_up_y+2) * SIZE, &img[17], SRCAND);
			putimage(left_up_x  * SIZE, (left_up_y+2) * SIZE, &img[18], SRCPAINT);
		}
	}
}//检查修补子弹打掉的草

/*清除子弹*/
void clear_zidan(Zidan d) {
	int x1 = d.x + img_zidan_detx1[d.dir];
	int y1 = d.y + img_zidan_dety1[d.dir];
	int x2 = d.x + img_zidan_detx1[d.dir] + SIZE-1;
	int y2 = d.y + img_zidan_dety1[d.dir] + SIZE-1;
	clearrectangle(x1, y1, x2, y2);
	check_add_zidan_grass(d);//检查补充草方块
	//先算出左上角坐标然后推算右下角坐标
}//清除子弹

/*检验子弹合法性*/
int check_zidan()
{
	Zidan d;
	for (int i = 1; i <= qz.size(); i++) //这么多个子弹要检查
	{
		d = qz.front();
		qz.pop();
		//注意xy实际上是弹头坐标，左上角坐标要加上偏移量（最上面）
		if (d.x == 0 || d.y == 0) {
			if (d.count) {
				clear_zidan(d);
			}
			else {
				d.count = 1;
			}//出界
			continue;
		}
		int xx = d.x + SIZE * dirx[d.dir];
		int yy = d.y + SIZE * diry[d.dir];//确定下一步位置
		if (xx > MAXX || yy > MAXY) {
			if (d.count) {
				clear_zidan(d);
			}
			else {
				d.count = 1;
			}//出界
			continue;
		}
		int checkX1 = xx / SIZE + check_if_movex[0][d.dir];
		int checkY1 = yy / SIZE + check_if_movey[0][d.dir];//确定检查点单位位置1
		int checkX2 = xx / SIZE + check_if_movex[1][d.dir];
		int checkY2 = yy / SIZE + check_if_movey[1][d.dir];//确定检查点单位位置2
		if (flag[yy/SIZE][xx/SIZE] ==-1 &&(!mat[checkY1][checkX1]&& !mat[checkY2][checkX2]
			|| mat[checkY1][checkX1]==3 && mat[checkY2][checkX2]==3)) {//都为会都为草位置，不用管
			if (d.count) {
				clear_zidan(d);
			}
			else {
				d.count = 1;
			}
			if (!tool.flag[checkY1][checkX1] && !tool.flag[checkY2][checkX2]&& !door.flag[checkY1][checkX1] && !door.flag[checkY2][checkX2]) {//不能打在道具上
				draw_zidan(d, xx, yy);//画子弹
				//这里要画出旋转后的,这个里面的左上角坐标正确
				d.x = xx;
				d.y = yy;//更新坐标
				qz.push(d);
			}
		}
		else if((mat[checkY1][checkX1]>0 || mat[checkY2][checkX2]>0)
			  && !((mat[checkY1][checkX1] ==3  || mat[checkY2][checkX2] == 3)
			  &&(flag[checkY1][checkX1] != -1 || flag[checkY2][checkX2]== -1)))//后面是为了保证草和坦克同时存在可以判定到下面，而不是这一层
		{//没有打到坦克,打到方块
			int check_grass = 0;
			if (!((mat[checkY1][checkX1] == 1 || mat[checkY2][checkX2] == 1) && d.who != 3)) //左右
			{//有一个铁块并且等级不够就不消除,代表被挡住了
			    //消除方块
				switch (d.dir) {
				case 0:
					remove_any(-2, -1, 1, -1, d);//传递的参数是子弹信息和相对于弹头的位置(四个方块),子弹分界的左右两个
					break;
				case 1:
					remove_any(0, -2, 0, 1, d);
					break;
				case 2:
					remove_any(-2, 0, 1, 0, d);
					break;
				case 3:
					remove_any(-1, -2, -1, 1, d);
					break;
				}
			}
			if (d.count) {
				if (d.count) {
					clear_zidan(d);
				}
				else {
					d.count = 1;
				}
			}
			else {
				d.count = 1;
			}
		}
		else if (flag[yy / SIZE][xx / SIZE] == 10) {//打到家
			clearrectangle(7 * SIZE * 4, 12 * SIZE * 4, 7 * SIZE * 4 + 8 * SIZE, 12 * SIZE * 4 + 8 * SIZE);
			putimage(7 * SIZE * 4, 12 * SIZE * 4, &img[24]);//家炸了
			return 0;
		}
		else {//代表炸到了坦克
		//根据编号看谁挂掉了
			if (d.who>3&&flag[yy / SIZE][xx / SIZE] == 0|| d.who <= 3 && flag[yy / SIZE][xx / SIZE]!=10)
			{
				//代表两股势力
				if (clear_tank(xx, yy, 0)) {
					//test();
					return 2;//代表游戏结束
				}
				if (d.count) {
					clear_zidan(d);
				}
				else {
					d.count = 1;
				}
			}
			else {
				if (d.count) {
					clear_zidan(d);
				}
				else {
					d.count = 1;
				}
			}
		}
	}
	return 1;
}//检验子弹合法性

/*根据坦克产生一个子弹*/
void add_zidan(Tank t) {
	int tmp_type = t.type;//可以取到子弹的伤害
	Zidan d;
	d.dir = t.dir;
	int xx, yy;
	switch (t.dir) {//根据方向和坦克的左上角坐标确定子弹占据空间
	case 0:
		xx = t.x + 2 * SIZE;
		yy = t.y;
		break;
	case 1:
		xx = t.x + 4 * SIZE;
		yy = t.y + 2 * SIZE;
		break;
	case 2:
		xx = t.x + 2 * SIZE;
		yy = t.y + 4 * SIZE;
		break;
	case 3:
		xx = t.x;
		yy = t.y + 2 * SIZE;
		break;
	}
	d.x = xx;
	d.y = yy;
	d.who = t.type;//123都是自己的形态，代表是自己,否则是对方的
	d.count = 0;
	qz.push(d);
}//根据坦克产生一个子弹

/*修复老家*/
void repair_map() {
	int num = sizeof(rep_map) / sizeof(rep_map[0]);
	for (int i = 0; i < num; i++) {
		int k = rep_map[i][0];
		int x1 = rep_map[i][1] * 4;
		int y1 = rep_map[i][2] * 4;
		int x2 = rep_map[i][3] * 4 - 1;
		int y2 = rep_map[i][4] * 4 - 1;//右下角方块的左上角坐标
		for (int xx = x1; xx <= x2; xx++) {
			for (int yy = y1; yy <= y2; yy++) {
				mat[yy][xx] = k;
			}
		}
		for (int xx = x1; xx <= x2; xx++) {
			for (int yy = y1; yy <= y2; yy++) {
				putimage(xx*SIZE, yy*SIZE, &img[16]);
			}
		}
	}
}//修复老家

/*检查道具*/
int check_tool(int index) {
	//炸弹停止升级修复
	int x=0, y=0;
	int max_num = 10000;//最多寻找次数
	switch (index) {
	case 1:
		while (max_num--) {
			srand(rand_seed);//重置时间种子
			rand_seed = rand();//随机种子链
			x = rand() % NUMX;
			y = rand() % NUMY;
			if (flag[y][x]>0 && flag[y][x] != 10)
				break;
		}
		if(max_num)//表示可以找到坦克
			if (clear_tank(x*SIZE, y*SIZE, 1)) {
				return 1;
			}
		break;
	case 2:
		stop_epoch = 300;//3秒
		break;
	case 3:
		if (level < 3) {
			up_date_type(level+1,0);//更换类型
			level++;
		}
		break;
	case 4:
		repair_map();
		break;
	}
	return 0;//游戏继续
}//检查道具

/*检查关于传送门*/
int CheckDoor(Tank t,int x,int y)
{
	//xy是下一步的单位坐标
	//检查是否进入传送门，只有正对传送门才能进入
	if (1 - door.type == t.dir % 2) {
		if (t.dir % 2) {//左右
			if (y != door.y2&&y != door.y1)
				return 0;//不能进入
			if (y==door.y1&&(t.dir == 1 && x+ 3 == door.x1|| t.dir == 3 && x == door.x1)) {
				return 1;//从第一个进入
			}
			else if(y==door.y2&&(t.dir == 1 && x + 3 == door.x2 || t.dir == 3 && x == door.x2)){
				return 2;//从第二个进入
			}
		}
		else {//上下
			if (x != door.x2&&x != door.x1)
				return 0;
			if (x==door.x1&&(t.dir == 2 && y + 3 == door.y1 || t.dir == 0 && y == door.y1)) {
				return 1;//从第一个进入
			}
			else if (x==door.x2&&(t.dir == 2 && y + 3 == door.y2 || t.dir == 0 && y == door.y2)) {
				return 2;//从第二个进入
			}
		}
	}
	return 0;
}//检查关于传送门

/*四个方向*/
void control_dir(int now_dir) {
	if (tank[0].dir != now_dir) //原来不是在这个
	{
		tank[0].dir = now_dir;
		draw_a_tank(tank[0], tank[0].x, tank[0].y);
		tank[0].bef_move = now_dir;//更新
	}
	else if (tank[0].bef_move >= type[tank[0].type].Speed)//可以移动
	{
		int xx = tank[0].x + dirx[tank[0].dir] * SIZE;
		int yy = tank[0].y + diry[tank[0].dir] * SIZE;//坐标
		int check_door = CheckDoor(tank[0],xx/SIZE,yy/SIZE);//检查关于传送门,根据返回值来看位置
		//更新位置
		if (check_door) {
			if (check_door == 1) {
				xx = door.arrx2*SIZE;
				yy = door.arry2*SIZE;
			}
			else {
				xx = door.arrx1*SIZE;
				yy = door.arry1*SIZE;
			}
		}
		int check = check_wz(xx, yy, 0);
		if (check_door||check) { //检查位置
			draw_a_tank(tank[0], xx, yy);//xx,yy表示更新后的位置
			if (check_door) {
				Sleep(200);
				mark_tank(tank[0].x, tank[0].y, -1);
				mark_tank(xx, yy, 0);
			}
			else {
				update_mark(tank[0].dir, xx, yy, 0);//普通移动更新坦克位置信息
			}
			tank[0].x = xx;
			tank[0].y = yy;
			tank[0].bef_move = 0;//更新时间
			if (check != 10) {
				check_tool(check);//检查道具
				tool.exist = 0;
			}
		}
	}
}//四个方向

/*控制其他坦克*/
void check_tank()
{
	for (int i = 1; i <= num_tank; i++) {//依次取出坦克
		if (!tank[i].state)continue;//已经没有了
		if (tank[i].bef_move >= type[tank[i].type].Speed) {
			//可能停留在原地的
			if (tank[i].stop_time) {
				tank[i].stop_time--;
				if (tank[i].bef_fight >= type[tank[i].type].Fight_det) {
					srand(rand_seed);//重置时间种子
					rand_seed = rand();//随机种子链
					int state = rand() % 100;
					if (state <= 50) {
						tank[i].bef_fight = 0;
						add_zidan(tank[i]);//添加一个子弹
					}
					else {
						tank[i].bef_fight = 0;
					}
				}
			}
			//表示要移动的
			else if (tank[i].dir_stop_time) {
				int xx = tank[i].x + dirx[tank[i].dir] * SIZE;
				int yy = tank[i].y + diry[tank[i].dir] * SIZE;//坐标
				int check_door = CheckDoor(tank[i], xx / SIZE, yy / SIZE);//检查关于传送门,根据返回值来看位置
				int dir;
				if (check_door) {
					if (check_door == 1) {
						xx = door.arrx2*SIZE;
						yy = door.arry2*SIZE;
						dir = door.dir2;
					}
					else {
						xx = door.arrx1*SIZE;
						yy = door.arry1*SIZE;
						dir = door.dir1;
					}
					tank[i].dir_stop_time = rand() % 3+2;
				}
				if (check_door||check_wz(xx, yy, i))
				{
					draw_a_tank(tank[i], xx, yy);//xx,yy表示更新后的位置
					if (check_door) {
						Sleep(200);
						mark_tank(tank[i].x, tank[i].y, -1);
						mark_tank(xx, yy, i);
					}
					else {
						update_mark(tank[i].dir, xx, yy, i);//普通更新坦克位置信息
					}
					tank[i].x = xx;
					tank[i].y = yy;
					tank[i].bef_move = 0;//更新时间
					tank[i].dir_stop_time--;
				}
				else
				{
					//先打一发
					if (tank[i].bef_fight >= type[tank[i].type].Fight_det) {
						tank[i].bef_fight = 0;
						add_zidan(tank[i]);//添加一个子弹
					}
					srand(rand_seed);//重置时间种子
					rand_seed = rand();//随机种子链
					int state = rand() % 100;
					tank[i].dir_stop_time = state % 10+1;//方向锁定次数
					if (state % 3==1) {
						tank[i].dir = (tank[i].dir + 1) % 4;
						if(state%2)
							tank[i].stop_time = state*2;//可能几个回合之内停在原地
					}
					else if(state % 3 == 2){
						tank[i].dir = (tank[i].dir - 1 + 4) % 4;
						if(state%2)
							tank[i].stop_time = state*2;//可能几个回合之内停在原地
					}
					else {
						tank[i].dir = (tank[i].dir + 2) % 4;//反向
					}
					draw_a_tank(tank[i], tank[i].x, tank[i].y);
				}
			}
			else {
				//换方向默认优先向着中间目标
				srand(rand_seed);//重置时间种子
				rand_seed = rand();//随机种子链
				int state = rand() % 100;
				//根据移动类型优化路径
				if (tank[i].move_type == 1) {
					if (abs(tank[i].x - MAXX / 2) * 2 / MAXX * 100 > state) {//距离中间远
						if (tank[i].x - MAXX / 2 >= 0) {
							tank[i].dir = 3;//左边
						}
						else {
							tank[i].dir = 1;//右边
						}
					}
					else if (abs(tank[i].y - MAXY) / MAXY * 100 > state) {//距离下面远
						tank[i].dir = 2;//向下走
					}
					else {
						tank[i].dir = state % 4;//随机换向
					}
				}
				else {
					if (abs(tank[i].y - MAXY) / MAXY * 100 > state) {//距离下面远
						tank[i].dir = 2;//向下走
					}
					else if (abs(tank[i].x - MAXX / 2) * 2 / MAXX * 100 > state) {//距离中间远
						if (tank[i].x - MAXX / 2 >= 0) {
							tank[i].dir = 3;//左边
						}
						else {
							tank[i].dir = 1;//右边
						}
					}
					else {
						tank[i].dir = state % 4;//随机换向
					}
				}
				tank[i].dir_stop_time = state % 10+1;//方向锁定次数
				draw_a_tank(tank[i], tank[i].x, tank[i].y);
			}
		}
		//控制射击
		if (tank[i].bef_fight >= type[tank[i].type].Fight_det) {
			srand(rand_seed);//重置时间种子
			rand_seed = rand();//随机种子链
			int state = rand() % 100;
			if (state <= 50) {
				tank[i].bef_fight = 0;
				add_zidan(tank[i]);//添加一个子弹
			}
			else {
				tank[i].bef_fight = 0;
			}
		}
		tank[i].bef_fight = (tank[i].bef_fight + 1) % 10000;//距离上次发射时间
		tank[i].bef_move = (tank[i].bef_move + 1) % 10000;//距离上次移动时间
	}
}//控制其他坦克

/*添加道具*/
void add_tool()
{
	int x, y;
	bool check = 0;
	while (!check) {
		srand(rand_seed);//重置时间种子
		rand_seed = rand();//随机种子链
		x = rand() % NUMX - 2;
		y = rand() % NUMY - 2;//左上角单位坐标
		check = 1;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				if (mat[y + i][x + j]||flag[y+i][x+j]!=-1)
					check=0;//不能落在有东西的上面
			}
		}
	}
	int index = rand() % 4 + 1;//道具种类
	for (int i = 0; i < 2; i++) {
		for (int j = 0; j < 2; j++) {
			tool.flag[y + i][x + j] = index;
		}
	}
	putimage(x*SIZE+2, y*SIZE+2, &img[25+index]);
	tool.exist = 1;
}//添加道具

/*判断一个区域能否存在坦克*/
int CheckIfCanExist(int x1,int y1,int x2,int y2){
	//传参是左上角和右下角的左上角坐标
	for (int i = y1; i <= y2; i++) {
		for (int j = x1; j <= x2; j++) {
			if (mat[i][j] && mat[i][j] != 3 || flag[i][j] != -1)
				return 0;
		}
	}
	return 1;
}

/*更新传送门*/
void UpDataDoor() {
	if (door.x1 >= 0) {//清除原有的
		if (door.type) {//左右
			clearrectangle(door.x1*SIZE, door.y1*SIZE, door.x1*SIZE + 4 * SIZE - 1, door.y1*SIZE + SIZE - 1);
			clearrectangle(door.x2*SIZE, door.y2*SIZE, door.x2*SIZE + 4 * SIZE - 1, door.y2*SIZE + SIZE - 1);
		}
		else {//上下
			clearrectangle(door.x1*SIZE, door.y1*SIZE, door.x1*SIZE + SIZE - 1, door.y1*SIZE + 4 * SIZE - 1);
			clearrectangle(door.x2*SIZE, door.y2*SIZE, door.x2*SIZE + SIZE - 1, door.y2*SIZE + 4 * SIZE - 1);
		}
		for (int i = 0; i <= 4; i++) {
			if (door.type) {
				door.flag[door.y1][door.x1 + i] = 0;
				door.flag[door.y2][door.x2 + i] = 0;
			}
			else {
				door.flag[door.y1 + i][door.x1] = 0;
				door.flag[door.y2 + i][door.x2] = 0;
			}
		}
	}
	//生成传送门
	door.type = MyRand() % 2;
	//door.type = 1;
	int num = 0;//当前找到的个数
	int x, y;//随机生成的一组坐标
	while (num < 2) {
		srand(rand_seed);//重置时间种子
		rand_seed = rand();//随机种子链
		x = rand() % (NUMX - 10)+ 6;
		y = rand() % (NUMY - 8 )+ 6;//左上角单位坐标
		//必须是空地(没有任何东西)
		bool if_ok = 1;//表示是否可以
		for (int i = 0; i <= 4; i++) {//检查四个点
			if (door.type) {//检查一排
				if (mat[y][x + i] || flag[y][x + i] != -1 || flag[y + 1][x + i] != -1 || num && abs(x - door.x1) + abs(y - door.y1) <40) {
					if_ok = 0;
					break;
				}
			}
			else {//检查一列
				if (mat[y+i][x ] || flag[y+i][x]!=-1 || flag[y + i][x + 1]!=-1 || num && abs(x - door.x1) + abs(y - door.y1) < 40) {
					if_ok = 0;
					break;
				}
			}
		}
		//接下来查找坦克的合法传送位置
		if (door.type) {//左右类型传送门传上下
			//先看上面有没有位置
			if (CheckIfCanExist(x,y-4,x+3,y-1)) {//表明上面可以传送
				if (num) {
					door.arrx2 = x;
					door.arry2 = y - 4;
					door.dir2 = 0;
				}
				else{
					door.arrx1 = x;
					door.arry1 = y - 4;
					door.dir1 = 0;
				}
			}
			else {//否则找下面
				if (CheckIfCanExist(x,y+1,x+3,y+4)) {
					if (num) {
						door.arrx2 = x;
						door.arry2 = y + 1;
						door.dir2 = 2;
					}
					else {
						door.arrx1 = x;
						door.arry1 = y + 1;
						door.dir1 = 2;
					}
				}
				else {
					if_ok = 0;//两边都找过还不可以
				}
			}
		}
		else {
			//先看左边有没有位置
			if (CheckIfCanExist(x-4, y, x-1, y+3)) {//表明左边可以传送
				if (num) {
					door.arrx2 = x - 4;
					door.arry2 = y;
					door.dir2 = 3;
				}
				else {
					door.arrx1 = x - 4;
					door.arry1 = y;
					door.dir1 = 3;
				}
			}
			else {//否则找右边
				if (CheckIfCanExist(x+1, y , x + 4, y +3)) {
					if (num) {
						door.arrx2 = x + 1;
						door.arry2 = y;
						door.dir2 = 1;
					}
					else {
						door.arrx1 = x + 1;
						door.arry1 = y;
						door.dir1 = 1;
					}
				}
				else {
					if_ok = 0;//两边都找过还不可以
				}
			}
		}
		if (!if_ok) {
			continue;//不行
		}
		//更新标记点
		for (int i = 0; i < 4; i++) {
			if (door.type) {
				door.flag[y][x + i] = 1;
			}
			else {
				door.flag[y + i][x] = 1;
			}
		}
		num++;
		putimage(x*SIZE + 1, y*SIZE + 1, &img[30+door.type]);
		if (num == 1) {
			door.x1 = x;
			door.y1 = y;
		}
		else {
			door.x2 = x;
			door.y2 = y;
		}
	}
}//更新传送门

/*开始游戏*/
void begin() {
	int num_t = 0;
	int num_door = 0;
	while (1) {
		if (KEY_DOWN(VK_UP)) {
			control_dir(0);
		}
		if (KEY_DOWN(VK_RIGHT)) {
			control_dir(1);
		}
		if (KEY_DOWN(VK_DOWN)) {
			control_dir(2);
		}
		if (KEY_DOWN(VK_LEFT)) {
			control_dir(3);
		}
		if (KEY_DOWN(VK_SPACE)) {
			if (tank[0].bef_fight >= type[tank[0].type].Fight_det) {//至少要过几个时间段才可以发射子弹
				tank[0].bef_fight = 0;
				add_zidan(tank[0]);//添加一个子弹
			}
		}
		if (KEY_DOWN(VK_ESCAPE)) {
			stop();
		}
		if (stop_epoch) {
			stop_epoch--;
		}
		else {
			check_tank();//其他坦克
		}
		Sleep(10);//更新频率
		int now_state = check_zidan();
		if (now_state == 2) //成功通关
		{
			PrintSucess();
			return;
		}
		else if (!now_state || my_lives == 0)//清除无效子弹，检查是否有坦克炸毁
		{
			PrintGameOver();
			return;
		}
		tank[0].bef_fight = (tank[0].bef_fight + 1) % 10000;//距离上次发射时间
		tank[0].bef_move = (tank[0].bef_move + 1) % 10000;//距离上次移动时间
		tool.num++;
		if (tool.num == tool_speed) {//要考虑刷新道具了
			tool.num = 0;
			if (!tool.exist) {
				add_tool();
				tool.exist = 1;
			}
		}
		door.num--;
		if (door.num == 0) {//更新传送门
			door.num = door_speed;//更新周期
			UpDataDoor();//更新传送门
		}
	}
}//开始游戏

void game() {
	initgraph(MAXX + 1 + 400, MAXY + 1, SHOWCONSOLE);
	ini_all();//全局初始化函数,一次游戏只初始化一次(区别于关卡初始化)

	for (int i = 0; i <= 2;i++)//关卡数字循环,当前是i关
	{
		now_level = i;
		printNextLevel();//打印转场动画
		while (1) {
			Sleep(10);
			settextcolor(WHITE);
			settextstyle(400, 40, 0);
			outtextxy(300, 300, "点击ENTER开始游戏...");
			if (KEY_DOWN(13)) {//点击enter进入
				cleardevice();
				int num = get_map(map,all_map[now_level]);//将当前的关卡地图从all_map中写到map中.并且返回数量
				ini_every(map, num);//关卡初始化
				ini_tank();//初始化出坦克
				draw_all_tank();//画坦克
				begin();//游戏开始,主控程序
				while (1) {
					Sleep(10);
					if (KEY_DOWN(VK_ESCAPE)) {//读取玩家开始信息
						cleardevice();
						break;
					}
				}
				break;
			}
		}
	}
}
```



 ',0,'2022-10-03 12:15:20','2022-10-03 12:15:20');

INSERT INTO article_detail(article_id,version,content,deleted,create_time,update_time) VALUES(5,1,_utf8mb4'​

下面是视频地址：[ 无人机DIY入门系列教程：(三）姿态解算上：姿态角、欧拉角与旋转矩阵_哔哩哔哩_bilibili](https://link.csdn.net/?from_id=130091824&target=https%3A%2F%2Fwww.bilibili.com%2Fvideo%2FBV1MK411d7RL%2F%3Fspm_id_from%3D333.337.search-card.all.click%26vd_source%3D3c53dd019471c050a181597ec42fada7)

# 坐标系
首先是引入机体坐标系和世界坐标系，都是xyz坐标系，下面是一张机体坐标系，实际上我在实验的时候，在三维图画也可以看到这两套坐标系，一套是不变的，应该就类似于世界坐标系，一套时在飞机上跟着飞机一起动的。

一般世界坐标系是东北天(东-x，北-y，上-z)，机体坐标系是右前上(下图)
![img](https://s21.ax1x.com/2025/11/27/pZEpm5t.png)

#  从图像看三种角
    首先是俯仰，也就是飞机抬头
![俯仰示意gif](https://i.cetsteam.com/imgs/2025/11/27/e790e44ce247ade4.gif)

     然后是航向，也就是类似于汽车在平地上转弯
![航向示意gif](https://i.cetsteam.com/imgs/2025/11/27/3872c1064bce4842.gif)

     最后是横滚，也就是飞机颠簸
![横滚示意gif](https://i.cetsteam.com/imgs/2025/11/27/d992d3104611c07b.gif)

**下面是三种角的引入**

为了好理解，假设水平面就是地面

    1.偏航角指的是**机体y**在水平面上的投影与世界y的夹角，也就是y自己在地上的投影和地上标准坐标y轴的夹角

    2.俯仰角指的是**机体y**与水平面的夹角，也就是y自己和地面的夹角

    3.横滚角指的是**机体z**与**机体y所在铅垂面的夹角**，一条空间中的线可以确定无数个平面，而与地面垂直的那个就作为铅垂面，这个也是线面角

从坐标系来看
![img](https://s21.ax1x.com/2025/11/27/pZEpuPP.jpg)
     如上图，xyz表示世界坐标系，x''y''z''表示机体坐标系，跟视频的保持一致，机头原始朝向y''位置

其中y''H是y‘在水平面的投影，y''Oy''H构成了y''所在的铅垂面，z''H表示z''在y''所在铅垂面的投影

根据定义：

    1. φ是y与水平面的夹角，为俯仰角

    2. α是y’在水片面投影与y的夹角，为偏航角

    3. β为z''与y''所在铅垂面的夹角，也就是z''Oz''H，为滚转角

# 如何理解角度
    任意飞机的位置都可以表示成上面的几种运动的组合，因此几个角度其实就是在描述标准坐标系如何旋转到当前的机体坐标系，也就是描述飞机如何从一开始转到现在这个状态

    偏航角和俯仰角其实好理解，就是**机体先偏航（左右旋转就是α），然后抬头（上下旋转是φ）**，可以想象这个过程，那么如何理解滚转角β？

    从几个定义中可以发现，x轴其实是没用的，为了简便我们先抛弃


![img](https://s21.ax1x.com/2025/11/27/pZEpK8f.jpg)
     观察上图，xy是初始坐标系（定为世界坐标系，z竖直向上），飞机起初朝向y位置，经过过偏航α到了y''H,此时z轴不变，然后经过俯仰，也就是飞机抬头φ，到了y‘位置，此时的z是会变的，变到了z'',相当于就是y''HOz在旋转，转到了y''Oz''

    此时**飞机头指向y''**,这时候实际上飞机还是正的，那么滚转实际上描述的就是飞机绕着飞机头所指向的轴y''进行旋转，这时的**z''活动范围就是一个与y’垂直，y‘为轴，z’为半径的圆盘，那么如何计算旋转的角度**，也就是计算**z''1(任意一个旋转到的位置)与z''的夹角，这个角就是β**

    回过头来，根据前面的定义，β是最终的飞机z与飞机y所在铅锤平面上的投影所成角度，这里的z''实际上等于与前面整体图的z''H，也就是最终z(z''1,z''2....任意旋转)在平面上的投影，因为z''是与y''Oz共面的，而zoy‘就是铅垂面(默认初始z竖直向上)，也就是y’所在铅垂面，而z''1是由y''Oz''旋转得到（绕机体所在轴旋转），显然投影也就是z''，这里说法可能不太科学，但是感觉一下也差不多。

    那z''相当于前面的z‘H，那么最终机体z也就是z1''和z''的夹角自然就是β了，这也就描述了飞机绕着轴转，也就是横滚。

​',0,'2023-04-01 20:40:12','2023-04-01 20:40:12');

INSERT INTO article_detail(article_id,version,content,deleted,create_time,update_time) VALUES(6,1,_utf8mb4'>手写MNIST识别是一个非常经典的问题，其数据集共有70000张28*28像素的图片，其中60000张作为训练集，剩下的10000张作为测试集，每一张图片都表示了一个手写数字，经过了灰度处理。
>本文延续前面文章提到的多层感知机，在这里完成更复杂的任务，这个也可以作为分类任务的代表。

# 1.导入数据
首先导入模型，我个人的习惯是在同名目录下新建static文件夹存放一些数据，这里先建好static/data，然后就可以等待下载数据了，文件夹关系如下：
![在这里插入图片描述](https://i.cetsteam.com/imgs/2025/11/27/eb9977f4ec12feeb.png)


 在这里，尽管导入的时候已经是batch的形式了，为了后面方便起见，我还将训练集和测试集分别按照numpy的格式组装为一个个batch，然后将标注进行独热编码，具体的代码可以看后面的**完整代码**。

# 2.训练模型
​
  训练模型是最复杂的部分，这里涉及到前向传播和反向传播，这部分在我前面的[博客](https://blog.csdn.net/weixin_60360239/article/details/138011921?spm=1001.2014.3001.5501)中提到过，不再赘述，不同的是多出了在每个隐藏层和输出层进行了**批量规范化（Batch Normalization）**，也就是BN层，在这里简单展开介绍一下。
  （感觉我自己写的部分可能只能我自己看得懂了...建议可以顺着我推荐的博客或者其他人的好好了解一下，我写的比较简单）

建议先看这两篇：
[1. 知乎，这一篇主要是介绍一些优点](https://zhuanlan.zhihu.com/p/52736691)

[2. 知乎，这一篇的推导非常详细](https://zhuanlan.zhihu.com/p/57847763)
​先考虑**在训练时**的情况，给出表达式，假设输入为$x$，输出为$o$，维度均为$n\times1$，那么在$BN$层中经过了如下变换：

...
省略公式,因为渲染不出来,如果有兴趣可以来踩一踩[我的博客](https://blog.csdn.net/weixin_60360239/article/details/138471500?spm=1001.2014.3001.5501)
...

# 3.模型运行
模型运行五轮之后的结果：（显示出来的都是预测的前面五个的结果）
![在这里插入图片描述](https://i.cetsteam.com/imgs/2025/11/27/f2da069f53c12665.png)

因为没有很多优化，代码运行的效率并不算好，只能说勉强能跑（在 batch_size = 32 的情况下大约两分钟训练完一轮），在训练完第一轮的时候在测试集的准确率就已经达到大约90%了，后面几轮的提升不大。
没试过其他batch_size或者调参之类的，能跑应该就是没问题的吧？
![在这里插入图片描述](https://i.cetsteam.com/imgs/2025/11/27/a18cd5caf46a6e26.png)

# 4.**完整代码**
下面贴出**完整代码**，写的有冗余，写法也很抽象，仅供学习参考哈（很多推导我在前面的[博客](https://blog.csdn.net/weixin_60360239/article/details/138011921?spm=1001.2014.3001.5501)都提到过，这一篇就没有多说了）

```python
# 手写Minist数据集分类
import numpy as np
import torch
import torchvision
from matplotlib import pyplot as plt
from numpy import log
from numpy.random import uniform
from sklearn.metrics import accuracy_score
from tqdm import tqdm

plt.rcParams[''font.sans-serif''] = [''SimHei'']
plt.rcParams[''axes.unicode_minus''] = False


def load_data(if_reload=False):
    batch_size_train = 32
    batch_size_test = 1
    random_seed = 1
    torch.manual_seed(random_seed)
    train_loader = torch.utils.data.DataLoader(
        torchvision.datasets.MNIST(''./static/data/'', train=True, download=True,
                                   transform=torchvision.transforms.Compose([
                                       torchvision.transforms.ToTensor(),
                                       torchvision.transforms.Normalize(
                                           (0.1307,), (0.3081,))
                                   ])),
        batch_size=batch_size_train, shuffle=True)
    test_loader = torch.utils.data.DataLoader(
        torchvision.datasets.MNIST(''./static/data/'', train=False, download=True,
                                   transform=torchvision.transforms.Compose([
                                       torchvision.transforms.ToTensor(),
                                       torchvision.transforms.Normalize(
                                           (0.1307,), (0.3081,))
                                   ])),
        batch_size=batch_size_test, shuffle=True)
    train_size = len(train_loader)
    test_size = len(test_loader)
    print(''训练规模:'', train_size)
    print(''测试规模:'', test_size)

    # 设置类别的数量
    num_classes = 10
    onehot = np.eye(num_classes)
    X_train = np.zeros((train_size, batch_size_train, 784, 1), dtype=np.float32)
    y_train = np.zeros((train_size, batch_size_train, 10, 1), dtype=int)  # 要独热编码
    X_test = np.zeros((test_size, batch_size_test, 784, 1), dtype=np.float32)
    y_test = np.zeros((test_size, batch_size_test, 1), dtype=int)  # 不需要进行独热编码
    # 训练
    for batch_idx, (data, target) in enumerate(train_loader):
        if data.shape[0] < batch_size_train:
            break
        X_train[batch_idx] = np.array(data).reshape(batch_size_train, -1, 1)  # 展平放入
        y_train[batch_idx] = onehot[target].reshape(batch_size_train, -1, 1)  # 将整数转为一个10位的one hot编码

    # 测试
    for batch_idx, (data, target) in enumerate(test_loader):
        if data.shape[0] < batch_size_test:
            break
        X_test[batch_idx] = np.array(data).reshape(batch_size_test, -1, 1)  # 展平放入
        y_test[batch_idx] = np.array(target).reshape(batch_size_test, -1, 1)
    return X_train, y_train, X_test, y_test


class MyNet():
    def __init__(self):
        self.eps = 0.00001  # 极小值
        self.new_rate = 0.1  # 均值新值所占的比例
        self.alpha_BN = 0.005

    # sigmoid
    def sigmoid(self, x):
        return 1 / (1 + np.exp(-x)) + self.eps  # 加上一个极小值

    # rule
    def rule(self, x):
        x[np.where(x < 0)] = 0
        return x

    # 损失函数
    def loss_func(self, output, label):  # 输入是batch*列向量
        out = log(output + self.eps)  # 加上一个极小值
        loss = -label.transpose(0, 2, 1) @ out
        return np.sum(loss.flatten())  # 总的损失(batch个)

    def softmax(self, out):  # 输入格式(batch,row,1),应该算出每个batch的最大值,然后减掉
        x = out.copy()
        res = np.zeros_like(x, dtype=np.float32)
        for i in range(x.shape[0]):
            x[i] -= max(x[i])
            expx = np.exp(x[i])
            sexpx = np.sum(expx)
            res[i] = expx / sexpx
        return res

    # batch norm
    def train_normlize(self, x, gamma, beta):  # 输入 (batch,rows,1)
        mmu = np.mean(x, axis=0)
        var = np.mean((x - mmu) ** 2, axis=0)  # rows,1
        ivar = 1 / np.sqrt(var + self.eps)  # rows,1
        x_hat = (x - mmu) * ivar  # [(batch,rows,1)-(rows,1)]/rows,1->batch,rows,1
        out = gamma * x_hat + beta
        return out, ivar, mmu, var, x_hat  # 后面mmu和var是为了累计总均值和标准差,所以传回,x_hat在更新系数γ需要用到

    def normlize_eval(self, x):  # 输入 (batch,rows,1)
        return (x - self.running_mmu) / self.running_var

    def fit(self, X, Y, test_x=None, test_y=None, epochs=30, test_epoch=1 ,alpha=0.01, layers=2, layer_size=3, activation=''sigmoid''):
        # layer,layer_size:中间的隐藏层层数和隐藏层的大小
        print(''fit ...'')
        self.acti_func = self.sigmoid
        if activation == "rule":
            self.acti_func = self.rule
        else:
            assert "unknown activation algorithm: %s" % (activation)
        batch_size, in_features = X[0].shape[0], X[0].shape[1]
        out_features = Y[0].shape[1]
        batchs = len(X)
        batch_size = X[0].shape[0]  # batch大小
        print(''batchs:'', batchs)
        samples = batch_size * batchs

        # 矩阵初始化(随机初始化)
        w_first = uniform(-1, 1, (in_features, layer_size))  # 每一行是一个输入神经元对所有下一层神经元的权值,+1是偏置的权值(有BN层不需要)
        w_last = uniform(-1, 1, (layer_size, out_features))  # 每一行是一个输入神经元对所有下一层神经元的权值
        self.w = None  # 防止后续报错
        if layer_size > 1:  # 刚好等于1就不需要了
            w = uniform(-1, 1, (layers - 1, layer_size, layer_size))  # 每一层都大小+1

        # 运行过程中的变量(注意有了BN层不需要偏置了)
        delta = np.zeros((batch_size, layers, layer_size, 1), dtype=float)  # 前面每一层都要+1
        delta_last = np.zeros((batch_size, out_features, 1), dtype=float)  # 每一列是一层的δ
        out_last = np.ones((batch_size, out_features, 1), dtype=float)  # 每一列是一层的output
        out = np.ones((batch_size, layers, layer_size, 1), dtype=float)
        out_softmax = np.ones((batch_size, out_features, 1), dtype=float)  # 经过softmax得到的结果
        norm_gamma = np.ones((layers, layer_size, 1), dtype=float)
        norm_gamma_last = np.ones((out_features, 1), dtype=float)  # BN的γ
        norm_beta = np.zeros((layers, layer_size, 1), dtype=float)
        norm_beta_last = np.zeros((out_features, 1), dtype=float)  # BN的β
        ivar = np.zeros((layers, layer_size, 1), dtype=float)  # 1/sqrt(σ^2+eps)
        ivar_last = np.zeros((layers, out_features, 1), dtype=float)  # 1/sqrt(σ^2+eps)
        x_hat = np.zeros((batch_size, layers, layer_size, 1), dtype=float)
        x_hat_last = np.zeros((batch_size, out_features, 1), dtype=float)  # 每一列是一层的output
        # 下面是运行过程中需要统计的均值和标准差
        running_mmu = np.zeros((layers, layer_size, 1), dtype=float)
        running_mmu_last = np.zeros((out_features, 1), dtype=float)
        running_var = np.zeros((layers, layer_size, 1), dtype=float)
        running_var_last = np.zeros((out_features, 1), dtype=float)

        # 开始迭代
        loss_list = []
        for epoch in range(1, 1 + epochs):
            loss = 0
            cnt = 0
            pbar = tqdm(zip(X, Y))
            for idx, (x, y) in enumerate(pbar):
                # 前向传播
                # 输入->...layers->layer_-1->softmax->argmax()
                for layer in range(layers + 2):
                    # 先标准化再激活
                    if layer == 0:  # 第一层
                        xx = (w_first.T @ x)
                        out[:, layer, :], tmp, mmu, var, x_hat_ = self.train_normlize(
                            x=xx, gamma=norm_gamma[layer], beta=norm_beta[layer])
                        out[:, layer, :] = self.acti_func(out[:, layer, :])
                        x_hat[:, layer, :] = x_hat_
                        ivar[layer] = tmp
                        running_var[layer] = running_var[layer] * (1 - self.new_rate) + var * self.new_rate
                        running_mmu[layer] = running_mmu[layer] * (1 - self.new_rate) + mmu * self.new_rate
                    elif layer < layers:  # 不是最后一层
                        xx = (w[layer - 1].T @ out[:, layer - 1])
                        out[:, layer, :], tmp, mmu, var, x_hat_ = self.train_normlize(
                            x=xx, gamma=norm_gamma[layer], beta=norm_beta[layer])
                        out[:, layer, :] = self.acti_func(out[:, layer, :])
                        x_hat[:, layer, :] = x_hat_
                        ivar[layer] = tmp
                        running_var[layer] = running_var[layer] * (1 - self.new_rate) + var * self.new_rate
                        running_mmu[layer] = running_mmu[layer] * (1 - self.new_rate) + mmu * self.new_rate
                    elif layer == layers:  # 最后一层隐藏层到输出层
                        xx = w_last.T @ out[:, layer - 1]
                        out_last[:, :, :], tmp, mmu, var, x_hat_ = self.train_normlize(
                            x=xx, gamma=norm_gamma_last, beta=norm_beta_last)
                        out_last = self.acti_func(out_last)
                        ivar_last = tmp
                        x_hat_last[:, :, :] = x_hat_
                        running_var_last = running_var_last * (1 - self.new_rate) + var * self.new_rate
                        running_mmu_last = running_mmu_last * (1 - self.new_rate) + mmu * self.new_rate
                    else:
                        out_softmax = self.softmax(out_last)
                # 反向传播
                for layer in range(layers, -1, -1):  # layers,...,0
                    # 计算出每一层的损失
                    if layer == layers:  # 最后一层(输出层),这里直接用softmax层计算得到
                        # print(''输出层'')
                        if activation == ''sigmoid'':
                            delta_last = out_last * (1 - out_last) * (out_softmax - y)
                            # 分类的导数实际和回归的一样,只不过多了个softmax过程,实际上还是一样的
                        elif activation == ''rule'':
                            deriva = np.ones_like(out_last)
                            deriva[np.where(out_last < 0)] = 0
                            delta_last = deriva * (out_softmax - y)
                    elif layer == layers - 1:  # 隐藏层最后一层,连接输出层
                        # print(''最后一层隐藏'')
                        if activation == ''sigmoid'':
                            delta[:, layer] = out[:, layer] * (1 - out[:, layer]) * (w_last @ delta_last)
                        elif activation == ''rule'':
                            deriva = np.ones_like(out[:, layer])
                            deriva[np.where(out[:, layer] < 0)] = 0
                            delta[:, layer] = deriva * (w_last @ delta_last)
                    else:  # 最后一层
                        # print(''隐藏'')
                        if activation == ''sigmoid'':
                            delta[:, layer] = out[:, layer] * (1 - out[:, layer]) * (
                                        w[layer] @ delta[:, layer + 1])  # 只有1-n会继续前向传播
                        elif activation == ''rule'':
                            deriva = np.ones_like(out[:, layer])
                            deriva[np.where(out[:, layer] < 0)] = 0
                            delta[:, layer] = deriva * (w[layer] @ delta[:, layer + 1])
                # 更新系数(w和两个BN参数)
                for layer in range(layers + 1):
                    if layer == 0:  # 输入-隐藏
                        # print(''输入-隐藏'')
                        x_hat_ = x_hat[:, layer, :]  # x_hat少一行1(相比x)
                        det_gamma = np.sum(x_hat_ * delta[:, layer, :], axis=0)  # batch,rows-1,1 x rows-1,1
                        det_beta = np.sum(delta[layer, :], axis=0)  # beta其实跟b是一样的(本来有了BN层的偏置就不需要b了)
                        var_sqr = ivar[layer]  # rows,1
                        dxhat = norm_gamma[layer] * delta[:, layer, :]  # γ(do/dx_hat)*δ(dL/do)
                        dx = 1 / batch_size * var_sqr * (batch_size * dxhat - np.sum(dxhat, axis=0) -
                                                         x_hat_ * np.sum(dxhat * x_hat_, axis=0))
                        det_w = x @ dx.transpose([0, 2, 1])
                        det_w = np.mean(det_w, axis=0)
                        w_first = w_first - alpha * det_w  # O_{Layer-1,i}δ_{Layer,j}
                        norm_gamma[layer] -= self.alpha_BN * det_gamma
                        norm_beta[layer] -= self.alpha_BN * det_beta
                    elif layer < layers:  # 隐藏-隐藏
                        # print(''隐藏-隐藏'')
                        x_hat_ = x_hat[:, layer, :]  # x_hat少一行1(相比x)
                        det_gamma = np.sum(x_hat_ * delta[:, layer, :], axis=0)  # batch,rows-1,1 x rows-1,1
                        det_beta = np.sum(delta[layer, :], axis=0)  # beta其实跟b是一样的(本来有了BN层的偏置就不需要b了)
                        var_sqr = ivar[layer]  # rows,1
                        dxhat = norm_gamma[layer] * delta[:, layer, :]  # γ(do/dx_hat)*δ(dL/do)
                        dx = 1 / batch_size * var_sqr * (batch_size * dxhat - np.sum(dxhat, axis=0) -
                                                         x_hat_ * np.sum(dxhat * x_hat_, axis=0))
                        det_w = out[:, layer - 1] @ dx.transpose([0, 2, 1])
                        det_w = np.mean(det_w, axis=0)
                        w[layer - 1] = w[layer - 1] - alpha * det_w  # O_{Layer-1,i}δ_{Layer,j}
                        norm_gamma[layer] -= self.alpha_BN * det_gamma
                        norm_beta[layer] -= self.alpha_BN * det_beta
                    else:  # 隐藏-输出
                        # print(''隐藏-输出'')
                        x_hat_ = x_hat_last  # x_hat少一行1(相比x)
                        det_gamma = np.sum(x_hat_ * delta_last, axis=0)  # batch,rows-1,1 x rows-1,1
                        det_beta = np.sum(delta_last, axis=0)  # beta其实跟b是一样的(本来有了BN层的偏置就不需要b了)
                        var_sqr = ivar_last  # rows,1
                        dxhat = norm_gamma_last * delta_last  # γ(do/dx_hat)*δ(dL/do)
                        dx = 1 / batch_size * var_sqr * (batch_size * dxhat - np.sum(dxhat, axis=0) -
                                                         x_hat_ * np.sum(dxhat * x_hat_, axis=0))
                        det_w = out[:, layer - 1] @ dx.transpose([0, 2, 1])
                        det_w = np.mean(det_w, axis=0)
                        w_last = w_last - alpha * det_w  # O_{Layer-1,i}δ_{Layer,j}
                        norm_gamma_last -= self.alpha_BN * det_gamma
                        norm_beta_last -= self.alpha_BN * det_beta
                soft_fal = np.argmax(out_softmax, axis=1).flatten()
                y_fal = np.argmax(y, axis=1).flatten()
                cnt += np.where(soft_fal == y_fal)[0].size
                this_loss = self.loss_func(out_softmax, y)
                loss += this_loss / batch_size  # 平均每个样本误差
            print(''当前训练轮整体准确率:%.2f%%'' % (100 * cnt / (batch_size * len(X_train))))
            loss_list.append(loss / samples)  # 样本平均误差
            print(''epoch %d loss:%.6f'' % (epoch, loss / samples))

            running_var *= batch_size / (batch_size - 1)
            running_var_last *= batch_size / (batch_size - 1)  # 无偏估计预测
            # 保存参数
            self.w_first = w_first
            self.w_last = w_last
            self.w = w
            self.in_features = in_features
            self.out_features = out_features
            self.layers = layers
            self.layer_size = layer_size
            self.running_mmu = running_mmu
            self.running_mmu_last = running_mmu_last
            self.running_var = running_var
            self.running_var_last = running_var_last
            self.norm_gamma = norm_gamma
            self.norm_gamma_last = norm_gamma_last
            self.norm_beta = norm_beta
            self.norm_beta_last = norm_beta_last

            if epoch % test_epoch == 0:  # 一轮测试一次
                if test_x is None or test_y is None:
                    continue
                print(''(!) test...'')
                pred = self.predict(test_x)
                display_num = 5  # 显示的个数
                for i in range(display_num):
                    print(''pred='', pred[i], '' true='', test_y[i])
                acc = accuracy_score(test_y.reshape(-1, 1), pred.reshape(-1, 1))
                print(''acc=%.2f'' % (acc))

        plt.plot(loss_list)
        plt.show()

    def predict(self, X):
        print(''test size:'',X.shape)
        w_first = self.w_first
        w_last = self.w_last
        w = self.w
        layers = self.layers
        out_features = self.out_features
        layer_size = self.layer_size
        batch_size = X.shape[1]  # 得到batch的大小
        running_mmu = self.running_mmu
        running_mmu_last = self.running_mmu_last
        running_var = self.running_var
        running_var_last = self.running_var_last
        norm_gamma = self.norm_gamma
        norm_gamma_last = self.norm_gamma_last
        norm_beta = self.norm_beta
        norm_beta_last = self.norm_beta_last

        out_last = np.ones((batch_size, out_features, 1), dtype=float)  # 每一列是一层的output
        out = np.ones((batch_size, layers, layer_size, 1), dtype=float)
        out_softmax = np.ones((batch_size, out_features, 1), dtype=float)  # 经过softmax得到的结果
        res = np.zeros((X.shape[0], batch_size, 1), dtype=int)
        for idx, x in enumerate(X):
            # 前向传播
            for layer in range(layers + 2):
                if layer == 0:  # 第一层
                    out[:, layer, :] = ((w_first.T @ x) - running_mmu[layer, :]) / np.sqrt(
                        running_var[layer, :] + self.eps)
                    out[:, layer, :] = norm_gamma[layer] * out[:, layer, :] + norm_beta[layer]
                    out[:, layer, :] = self.acti_func(out[:, layer, :])
                elif layer < layers:  # 不是最后一层
                    out[:, layer, :] = ((w[layer - 1].T @ out[:, layer - 1]) - running_mmu[layer, :]) / np.sqrt(
                        running_var[layer, :] + self.eps)
                    out[:, layer, :] = norm_gamma[layer] * out[:, layer, :] + norm_beta[layer]
                    out[:, layer, :] = self.acti_func(out[:, layer, :])
                elif layer == layers:  # 最后一层隐藏层到输出层
                    out_last = (w_last.T @ out[:, layer - 1] - running_mmu_last) / np.sqrt(running_var_last + self.eps)
                    out_last = norm_gamma_last * out_last + norm_beta_last
                    out_last = self.acti_func(out_last)
                else:
                    out_softmax = self.softmax(out_last)
            output = np.argmax(out_softmax, axis=1)
            res[idx] = output
        return res


if __name__ == ''__main__'':
    print(''******** minist手写数据集分类 *********'')
    X_train, y_train, X_test, y_test = load_data(if_reload=True)  # 获取标准化后的数据

    myNet = MyNet()
    myNet.fit(X_train, y_train, test_x=X_test, test_y=y_test, epochs=5, test_epoch=1, alpha=0.01, layers=2, layer_size=200,
              activation=''rule'')

    print(''训练完毕'')
    print(''test...'')
    pred = myNet.predict(X_test)
    display_num = 5  # 显示的个数
    for i in range(display_num):
        print(''pred='', pred[i], '' true='', y_test[i])
    acc = accuracy_score(y_test.reshape(-1, 1), pred.reshape(-1, 1))
    print(''acc=%.2f'' % (acc))
```
差不多就是这样了，有什么问题的话可以欢迎指出
',0,'2024-02-04 17:12:35','2024-02-04 17:12:35');

INSERT INTO article_detail(article_id,version,content,deleted,create_time,update_time) VALUES(7,1,_utf8mb4'​

# 简单介绍
Tarjan主要基于深搜，其中有两个非常关键的标记数组，分别是dfn和low，同时引入概念时间戳tt，也就是到达这个点的时间，实际上就是搜索到的次序，dfn记录每个点的时间戳，即第一次访问到的号次，low也就是能到达的最早的时间戳，下面分析。

# 缩点
题目来源：[【模板】缩点 - 洛谷](https://www.luogu.com.cn/problem/P3387)

    这一道题题意比较明显，就是你去走出一条路，点权之和最大，可以随便走，但是重复的点只算一次。

    思考了就知道，如果有那么一个**环(环就是1->2->3->1这样的)**在，那么一定是要把环上面的点全部拿来的，因为你可以走一圈然后回到原来的地方，那么何乐而不为呢？于是我们可以把一个环的权值全部加到一点，然后重新建一张图，此时的图就是有向无环图，后面利用拓扑排序和dp，可以确定最大的权值。

    如何缩点是这一题的重头戏，Tarjan算法基于深搜，每次会一股脑一直向下走，可以料想如果走到一个点发现走过了，那么再走上去是不是就成了一个环。

    引入dfn数组存储时间戳，low存储这个点可以到达的点的最早时间戳，栈st存储点的情况，用于统计哪些点是在环上的，vis用于当前这一轮找点的情况。

    得到一个点x，首先先入栈，然后将点的dfn和low均初始化为时间戳tt，然后开始查看与这个点相连的点，假设是to，如果这个点还没被访问过，也就是时间戳还是0，那么继续深搜，然后更新这个点的low值，也就是low[x]=min(low[x],low[to])；如果时间戳不是0，那么就看vis看是不是访问过，然后还是执行上面句子，**为什么是这样呢，因为第一种是没有访问过，那么这个点的low值你是不知道的，所以无法更新**，而访问过的可以直接得到值。在更新完所有与这个点连接的点之后就得到了这个点最终的low值。

    在一轮更新之后，此时**有着相同low值的就构成了一个强联通图**，其中任意两点都可以互相达到。为什么？因为我们在更新时遇到访问过的点会停止搜索，那么遇到访问过的那么这个点的时间戳一定早于我走来**到达这个点的这一条环(深搜是一条边走到黑)**任意一点的时间戳，在回溯进行low[x]=min(low[to],low[x])的操作之后，这一条环上的所有low值都被置为了访问到的那个点的，一条环一定是强连通图，因为更新的过程中有许多交叉，其实都会被置为最小的那个，所以最终一个low相同的图可能由多个环组成，环组成的还是强连通图。

    因为相同low值的是一个强连通图，而这个low值其实就是这个连通图最早被搜索到的值，我们就可以把这个点，也就是low[x]=dfn[x]的点作为这个强连通图的代表点，把这个连通图缩到这个点，至于如何找到这个代表点所代表的的块的所有节点就利用栈，一直弹到这个代表点出栈为止(因为这个点的low最小，所以最早进入栈)。

程序如下：
```cpp
void tarjan(int x)
{
	vis[x] = 1;
	st.push(x);
	low[x] = dfn[x] = ++tt; // 时间戳初始化
	for (int i = last1[x]; i; i = e1[i].next)
	{
		int to = e1[i].to;
		//双层次判断,dfn是全局标记,vis是当前轮标记
		//dfn=0表示这个点还没有被纳入任何环,也没走过,这时候需要继续往下走,找完更新(回溯更新)
		//vis!=0表示当前轮走过这个点然后又走到了,说明走出了一个环,那么直接更新,不用再找
		if (!dfn[to]) //这个点还没有时间戳,走下去
		{
			tarjan(to);
			low[x] = min(low[to], low[x]); //回溯的时候的更新
		}
		else if(vis[to])
			low[x] = min(low[to], low[x]); //走到了一个点这一轮已经被访问过了,这就说明走出一个圈了
	}
	if (low[x] == dfn[x])//说明是关键点,关键点权重就是整个环的权重(把换上其他点权重加上来)
	{
		int tmp;
		while (!st.empty())
		{
			tmp = st.top();
			st.pop();
			vis[tmp] = 0;	    //清除标记,因为是每一轮的标记(每次找环要清除标记)
			squ[tmp] = x;
			if (x == tmp)break;//表示到了环的根节点
			p[x] += p[tmp];    //缩点
		}
	}
}
```
这个过程比较抽象，举个例子，简单起见如下图所示：

    接下去因为要计算权值最大，我们在缩点之后重新建图，就可以得到一张新的无环有向图，利用dis[x]=min(dis[x],dis[to]+p[x])加上拓扑排序就可以计算出答案。

**完整代码**：
```cpp
#include<stdio.h>
#include<algorithm>
#include<stack>
#include<queue>
#define Inf 0x3f3f3f3f
#define N 11000
#define M  110000
using namespace std;
int n, m, p[N];
bool vis[N];
int low[N], dfn[N],tt;//最小时间戳,当前时间戳,时间戳
int squ[N];//存储每个点所属的连通块的关键点
int du[N];//存储每个点的入度
int dis[N];//存储每个点的大小
stack<int>st;//存储暂时的答案序列(一个环的)
queue<int>q;//topo排序会用到
struct Edge
{
	int next, from, to;
}e1[M*5],e2[M*5];
int last1[N], last2[N], cnt1,cnt2;

void add(int from, int to, Edge e[],int last[],int &cnt)
{
	e[++cnt].to = to;
	e[cnt].from = from;
	e[cnt].next = last[from];
	last[from] = cnt;
}

 //tarjan算法本质就是找出一个个圈,因为只要一走到走过的点就形成一个环,此时是强连通图,可以缩成一个点
void tarjan(int x)
{
	vis[x] = 1;
	st.push(x);
	low[x] = dfn[x] = ++tt; // 时间戳初始化
	for (int i = last1[x]; i; i = e1[i].next)
	{
		int to = e1[i].to;
		//双层次判断,dfn是全局标记,vis是当前轮标记
		//dfn=0表示这个点还没有被纳入任何环,也没走过,这时候需要继续往下走,找完更新(回溯更新)
		//vis!=0表示当前轮走过这个点然后又走到了,说明走出了一个环,那么直接更新,不用再找
		if (!dfn[to]) //这个点还没有时间戳,走下去
		{
			tarjan(to);
			low[x] = min(low[to], low[x]); //回溯的时候的更新
		}
		else if(vis[to])
			low[x] = min(low[to], low[x]); //走到了一个点这一轮已经被访问过了,这就说明走出一个圈了
	}
	if (low[x] == dfn[x])//说明是关键点,关键点权重就是整个环的权重(把换上其他点权重加上来)
	{
		int tmp;
		while (!st.empty())
		{
			tmp = st.top();
			st.pop();
			vis[tmp] = 0;	    //清除标记,因为是每一轮的标记(每次找环要清除标记)
			squ[tmp] = x;
			if (x == tmp)break;//表示到了环的根节点
			p[x] += p[tmp];    //缩点
		}
	}
}

 //topo排序+dp
int topo()
{
	for (int i = 1; i <= n; i++)
		if (squ[i] == i && du[i] == 0) //关键点入队
		{
			q.push(i);
			dis[i] = p[i];
		}
	while (!q.empty())
	{
		int x = q.front();
		q.pop();
		for (int i = last2[x]; i; i = e2[i].next)
		{
			int to = e2[i].to;
			du[to]--;
			dis[to] = max(dis[to], dis[x] + p[to]);
			if (du[to] == 0)q.push(to); //度为0入队
		}
	}
	int ans = 0;
	for (int i = 1; i <= n; i++)
		ans = max(ans, dis[i]);
	return ans;
}

int main()
{
	scanf("%d%d", &n, &m);
	for (int i = 1; i <= n; i++)
		scanf("%d", p + i);
	int x, y;
	for (int i = 1; i <= m; i++)
	{
		scanf("%d%d", &x, &y);
		add(x, y, e1, last1, cnt1); // 全局变量传进去也只是形参
	}
	for (int i = 1; i <= n; i++)
		if (!dfn[i]) //没有时间戳代表是没访问过
			tarjan(i);
	for (int i = 1; i <= m; i++)
	{
		int x = squ[e1[i].from];
		int y = squ[e1[i].to];
		if (x != y) // 去除自环
		{
			add(x, y, e2, last2, cnt2);
			du[y]++;
		}
	}
	int ans = topo();
	printf("%d\n", ans);
	return 0;
}
```
# 割点
题目来源：[【模板】割点（割顶） - 洛谷 ](https://www.luogu.com.cn/problem/P3388)

    割点就是在无向图中，一个点去掉了，图就不再连通了，那么这个点就是割点。

    那么如何寻找割点，实际上也是利用tarjan算法， 各种定义类似于上题，不过因为在无向图中，任意连通块总是强连通图，这就使得上题定义的low失去了意义，因为只要连通，那么最终所有节点的low值均相等，所以在这里的low的更新方式稍稍改变，也就是程序中的low[x] = min(low[x], dfn[to])。

    割点有两种情况，假设有下面这么一张图：



    第一种是对于根节点(最开始的点，一个连通块仅一个，随便设置)，因为深搜每次会搜索完一块与根节点相连的，要是根节点连接了两块或更多，那么这个根节点就是割点。假设1是根节点，那么第一次搜索完2、3,第二次搜索完4、5，有两块，所以1是割点。

    第二种情况是像2不是根节点的，这种就需要判断与他相连的点能不能不通过这个点到达更早的点，比如3只能通过2到达1，而5除了通过4，还可以通过6。

    设定根节点root，x点进入，先初始化low和dfn为时间戳，开始检查所有相连点to，如果dfn！=0也就是已经访问过，那么直接更新**low[x] = min(low[x], dfn[to])**，为什么是dfn[to]而不是low[to]，首先无向图都是双向边，要是low[to]那么全都是一样了，**这样更新之后low存储了所有直接相连边中的时间戳最小值**，也是为了下面的判断;如果dfn=0也就是没访问过，那么继续搜索，计算出low[to]后更新low[x]=min(low[x],low[to])，要是low[to]>=dfn[x]的话，就说明下一个点找遍也无法找出比x更早出现的点，所以x为割点。

部分代码：
```cpp
void tarjan(int x,int root)
{
	int child = 0;
	low[x] = dfn[x] = ++tt;
	for (int i = last[x]; i; i = e[i].next)
	{
		int to = e[i].to;
		if (!dfn[to]) //这个点还没有被找过,那么继续找,可能会找到更早的
		{
			tarjan(to, root);
			low[x] = min(low[to], low[x]);//更新当前值,如果能找到更早的,传给x
			if (low[to] >= dfn[x] && x != root)
				//相当于说把下一个点to找遍了相连的,找不到一个直接与to相连的点比x更早出现
				flag[x] = 1;
			if (x == root)child++;//child就是一个个块
			//根节点遍历一次找一个块,这个块与其他与根节点相连的块只能通过根节点相连(因为一旦dfn!=0就不tarjan了)
			/* example:
			2  1  3  5  6
			      4
			*/
			//假设3是根节点,3第一次找把21找了,child=1,第二次找了56,child=2,,第三次找了4,child=3>2,所以3是割点
		}
		low[x] = min(low[x], dfn[to]);
		//这个地方一定要注意！如果把dfn写做low的话那么一个连通块的low实际上都是根节点low值
		//low[x]始终存储与x相连的最早出现的时间戳
	}
	if (x == root && child >= 2)
		flag[root] = 1;
}
```
    结合判断条件low[to]>=dfn[x]，还有搜索后的low[x]=min(low[x],low[to])再来理解一下为什么是dfn[to]。

    首先因为每次遇到一个相连的点，如果to 没有访问过，那么就会对to进行深搜，由于low[x]=min(low[x],low[to])的存在，那么如果to能够通过其他路径到达了比x更早的位置，那么就可以继承其low值从而使得low[to]<dfn[x]。比如x=4，to=5，5可以通过6继承到1的low值，这个值比4的dfn要小，所以low[5]<dfn[4]，4不是割点。



要是没有其他路可以走到更早的位置，那么最终low[to]>=dfn[x]，那么这个点就是割点。比如

2->3,3没有路可以走到2的前面，所以low[3]=dfn[2]，2作为割点。 



   其余细节不再赘述。

**完整代码**：
```cpp
#include<stdio.h>
#include<algorithm>
using namespace std;
#define Inf 0x3f3f3f
#define M 500000
#define N 50000
bool flag[N];//存储点是否为割点
int num;	 //存储割点个数
int n, m;
int dfn[N], low[N], tt;
//low存储可以连到的最早出现的时间戳
struct Edge
{
	int to, next;
}e[M*5];
int last[N], cnt;

void tarjan(int x,int root)
{
	int child = 0;
	low[x] = dfn[x] = ++tt;
	for (int i = last[x]; i; i = e[i].next)
	{
		int to = e[i].to;
		if (!dfn[to]) //这个点还没有被找过,那么继续找,可能会找到更早的
		{
			tarjan(to, root);
			low[x] = min(low[to], low[x]);//更新当前值,如果能找到更早的,传给x
			if (low[to] >= dfn[x] && x != root)
				//相当于说把下一个点to找遍了相连的,找不到一个直接与to相连的点比x更早出现
				flag[x] = 1;
			if (x == root)child++;//child就是一个个块
			//根节点遍历一次找一个块,这个块与其他与根节点相连的块只能通过根节点相连(因为一旦dfn!=0就不tarjan了)
			/* example:
			2  1  3  5  6
			      4
			*/
			//假设3是根节点,3第一次找把21找了,child=1,第二次找了56,child=2,,第三次找了4,child=3>2,所以3是割点
		}
		low[x] = min(low[x], dfn[to]);
		//这个地方一定要注意！如果把dfn写做low的话那么一个连通块的low实际上都是根节点low值
		//low[x]始终存储与x相连的最早出现的时间戳
	}
	if (x == root && child >= 2)
		flag[root] = 1;
}

void add(int from, int to)
{
	e[++cnt].to = to;
	e[cnt].next = last[from];
	last[from] = cnt;
}

int main()
{
	scanf("%d%d", &n, &m);
	int x, y;
	for (int i = 1; i <= m; i++)
	{
		scanf("%d%d", &x, &y);
		add(x, y);
		add(y, x);//无向图双向建边
	}
	for (int i = 1; i <= n; i++)
		if (!dfn[i])
			tarjan(i,i);//把i作为根节点,寻找割点
	for (int i = 1; i <= n; i++)
		printf("i=%d low=%d dfn=%d\n", i, low[i], dfn[i]);
	for (int i = 1; i <= n; i++)
		if (flag[i])
			num++;
	printf("%d\n", num);
	for (int i = 1; i <= n; i++)
		if (flag[i])
			printf("%d ", i);
	printf("\n");
	return 0;
}
```
感觉脑子已经一片空白了，就这样吧

​',0,'2023-01-18 12:12:22','2023-01-18 12:12:22');

INSERT INTO article_detail(article_id,version,content,deleted,create_time,update_time) VALUES(8,1,_utf8mb4'​
**Problem Statement**
You are the top spy of AtCoder Kingdom. To prevent the stolen secret from being handed to AlDebaran Kingdom, you have sneaked into the party where the transaction happens.

There are NN attendees in the party, and they are given attendee numbers from 11 through NN. The height of Attendee ii is A_iAi​.

According to an examination beforehand, you know that a pair of attendees satisfying the condition below will make the transaction.

**·** The absolute difference of their attendee numbers is equal to the sum of their heights.
There are \frac{N(N-1)}{2}2N(N−1)​ ways to choose two from the NN attendees and make a pair. Among them, how many satisfy the condition above?

P.S.: We cannot let you know the secret.

**Constraints**
All values in input are integers.
2 \leq N \leq 2 \times 10^52≤N≤2×105
1 \leq A_i \leq 10^9\ (1 \leq i \leq N)1≤Ai​≤109 (1≤i≤N)
**Input**
Input is given from Standard Input in the following format:
```
NN
A_1A1​ A_2A2​ \dots… A_NAN
```
**Output**
Print the number of pairs satisfying the condition.

**Sample Input 1 Copy**
Copy

6
2 3 3 1 3 1
**Sample Output 1 Copy**
Copy
```
3
A_1 + A_4 = 3A1​+A4​=3, so the pair of Attendee 11 and 44 satisfy the condition.
A_2 + A_6 = 4A2​+A6​=4, so the pair of Attendee 22 and 66 satisfy the condition.
A_4 + A_6 = 2A4​+A6​=2, so the pair of Attendee 44 and 66 satisfy the condition.
```
No other pair satisfies the condition, so you should print 33.

 一道atcoder的题目，也确实是以前没有想过的思路，做个记录。

题目需要的就是下标差绝对值和高度之和相等的配对数目

思路
用h数组存储高度，那么表达式也就是 abs(i-j)==h[i]+h[j],假设 i>j (无所谓的),那么去掉绝对值也就是 i - j == h[i] +h[j] ，移项得到 i-h[i]==j+h[j]，所以只需要找到这样的i和j的数量。

引入map<>存储满足和i匹配的j+h[j]的j的数量，可以看到对于任意一组i，j，在扫描到i时， j 是匹配的，在扫描到 j 时，i 一定也是满足式子的，所以可以把计算分为两个阶段，扫描 i 时标记这一对，扫描到 j 时将这一对收集到答案中，体现到代码中其实就是先ans+然后再mp++的问题。而答案就是由这样的一组组 i j 组成，可解。

代码还是很短的
```cpp
#include<stdio.h>
#include<map>
#define ll long long
using namespace std;
ll h[1000000];
map<ll, ll>mp;
int main()
{
	int n;
	scanf("%d", &n);
	for (int i = 1; i <= n; i++)
		scanf("%lld", h + i);
	ll ans = 0;
	for (int i = 1; i <= n; i++) {
		ans += mp[i - h[i]];
		mp[i + h[i]]++;
	}
	printf("%lld", ans);
	return 0;
}
```

​',0,'2022-07-13 15:10:42','2022-07-13 15:10:42');

INSERT INTO article_detail(article_id,version,content,deleted,create_time,update_time) VALUES(9,1,_utf8mb4'​
最近学习数据库语言sql，学到了存储过程和游标这一块，上课一点没听，可以说是全程懵逼。不过好在有个课后的实验，然而cmd中的报错往往极其粗糙，只会告诉你什么附近有错（有时候还是错的），在不知error了多少次，破防多少次之后终于完成了老师的任务，觉得有必要写一篇记录一下。

~~全程cmd深空黑背景，胆小勿入。~~

首先先给出我自己定义的表的结构：


![img](https://s21.ax1x.com/2025/11/27/pZEpJVs.png)
 简单介绍一下，这是一张员工信息表，empno就是员工号，ename员工姓名，job就是工作名，heredate工作时间，salary是薪资，comm是补贴，deptno是单位号，mgr是上司员工号。数据都是乱凑的，请勿当真。

**存储过程**在我理解来其实就是编程语言里面常说的函数，了解过其他语言的同学对于函数不会陌生，不过这个名字是真的高大上，本质就是把值传进去，然后计算好把值传出来。

下面直接贴出一道真题：

![img](https://s21.ax1x.com/2025/11/27/pZEp3rQ.png)
这道题就是根据输入empno然后你写一个存储过程，然后在其中计算号年收入，然后把答案输出。思路非常简单，就是根据员工号找到对应员工然后找到他的月薪和补贴（我就当做是每月补贴了），乘以一年的数量就是一年收入。

那么问题来了，该怎么写这个存储过程？存储过程的关键字是precedure，我们create一个新的precedure，然后前面说了存储过程就是函数，你得给他要算的人，所以把参数列表里面的empno前面加上一个in，表示是输入，因为你还要把值输出的，sql里面有没有return我不知道，但是我可以把参数列表里面的变量标记为out，表示输出，类似于其他语言的引用传参，如下;
```sql
create procedure cal_all_salary(in empno_in int,out all_salary double)
begin
    过程体
end
```
 cal_all_salary就是这个过程的名字，empno_in前面有了一个in，表示是输入，all_salary前面有个out，表示是输出，当然这个参数是需要你自己传进来的，然后begin和end就是过程的开始和结束的标志。

过程中要怎么写，无非就是把all_salary重新赋值，需要用到select...into变量from...方法，这个变量就是所需要赋值的。
```sql
select (comm+salary)*12 into all_salary from eemp where empno=empno_in;
```
当然这还不是一个完整的，因为还要涉及到过程的结束开始符，完整如下：
```sql
drop procedure if exists cal_all_salary;
delimiter //
create procedure cal_all_salary(in empno_in int,out all_salary double)
begin
select (comm+salary)*12 into all_salary from eemp where empno=empno_in;
end;
//
delimiter ;
```
因为是用记事本写的，所以也没啥缩进，格式也比较拉胯。第一句主要是为了避免名称重复，有重复就覆盖掉。delimiter 字符串表示把这个字符串作为结束符，我自己用//比较多，这是为了避免跟过程体内的 ; 搞混而设置的。

然后测试，可以利用set @变量名来定义一个变量：
```sql
set @ans=0;
cal_all_salary(1,@ans);
select @ans;
```
**效果**
![img](https://s21.ax1x.com/2025/11/27/pZEp1Kg.png)


所以说存储本身就是为了完成一个特定的任务，把完成这个任务的方法存储起来，因此功能性也比较明确，方便了数据库的管理。

接下来是游标。游标在我觉得就是循环，把符合条件的打包成一个集合，然后一个个取出来判断，进行计算。

游标的使用格式如下：（抄了别人的）
```sql
DECLARE 迭代变量名1
.
.
.
DECLARE 游标名字 CURSOR FOR 查询语句;
open 游标名
while/repeat..
fetch 游标名 into var_name...
close 游标名
```
大致就是上面几个步骤。首先我先把有用的几个属性打包起来放进游标内，格式就是{[属性1,属性2...],[属性1,属性2...]...}，每一个游标指向一行，有着多个属性的值，然后我利用循环一条一条取出来然后去做我想做的事情。这里的fetch就是取值，然后分别赋值给对应的变量。需要注意的就是打开和关闭。

最后贴一道题：
![img](https://s21.ax1x.com/2025/11/27/pZEp8bj.png)

代码如下，应该是正确实现了的
```sql
drop procedure if exists avg_from_dept;
delimiter //
create procedure avg_from_dept(in dept_code int,out ans double)
begin
declare now_salary double;
declare no_data int default 0;
declare sum double default 0;
declare dept_cursor cursor for select salary from eemp where deptno=dept_code;
declare exit handler for not found set no_data = 1;
open dept_cursor;
repeat
fetch dept_cursor into now_salary;
set sum=sum+now_salary;
set ans=sum/(select count(*) from eemp where deptno=dept_code);
until no_data=1
end repeat;
close dept_cursor;
end;
//
delimiter ;

set @dept_in=1;
set @ans=0;
call avg_from_dept(@dept_in,@ans);
select @ans;
```
都是记事本写的，格式很差...

​',0,'2022-12-02 22:45:17','2022-12-02 22:45:17');

INSERT INTO article_detail(article_id,version,content,deleted,create_time,update_time) VALUES(10,1,_utf8mb4'​

目录
[Dijkstra的优化](#Dijkstra的优化)

[P1119 灾后重建](#P1119 灾后重建)

[P1629 邮递员送信](#P1629 邮递员送信)

[P1144 最短路计数](#P1144 最短路计数)

[P1462 通往奥格瑞玛的道路](#P1462 通往奥格瑞玛的道路)

[P1522 [USACO2.4] 牛的旅行 Cow Tours](#P1522 [USACO2.4] 牛的旅行 Cow Tours)

# Dijkstra的优化
这个算法是最短路问题中最经典的的算法之一，用于求解单源最短路问题，也就是两点之间最短路，实际上可以求出一个点到其他所有点的最短路。常规的写法复杂度在O(n^2)，可以参考我之前写的一篇博客：图论---最小生成树，最短路径_czc131的博客-CSDN博客_最小生成树最短路径。

回顾一下求解的过程:首先是找出当前的距离最短最为确定的最短路，然后根据这个点来更新其他点到起点的距离。

优化的点有两个：

找当前距离最小点，这时候可以想到堆优化，当然实际的应用就是用STL的优先队列。队列中存储点的号次和到起点的距离，在入队时优先队列就会自动根据距离把最小的放到队首，这时候你取出队首，那么一定是距离最短的，设定一个vis数组标记，如果当前队首已经被标记为最短路，那么直接弹掉就好，否则更新，在更新的过程中，如果最小距离变了，就要入队，因为可能成为当前的最短路。
是更新过程，这个优化一般是针对于用邻接矩阵存储，同时边又比较少。邻接矩阵存储，在更新时很多遍历其实是无效的，这时候存边效率更高。引入链式前向星，链式表示是个链表，前向也就是指针是指向前面的，对于每条边建立结构体，定义如下：
```cpp
struct Edge
{
	int to, next;
	double len;
}e[M];
int last[N], cnt;
Edge表示边，to表示这条边连向的点，next表示同样是这个点为起点的上一个这条边的位置，定义last数组存储每个点为起点最后一条边的位置，类似于链表的头指针，cnt记录边的数量，还有加边过程就不多说了。

最短路更新的过程与原来相似，取出最短的点，然后更新这个点连出去的所有点。

假设计算k到其他所有点的最短路，代码如下：

void dksa(int k)
{
	dis[k] = 0;
	q.push({ k, 0 });
	while(!q.empty())
	{
		node x = q.top();
		q.pop();
		int d = x.d;
		if (vis[d])continue;//如果已经处理过了那么不需要处理了
		vis[d] = 1;
		for (int i = last[d]; i; i = e[i].next)
			if (dis[e[i].to] > dis[d] + e[i].len)
			{
				dis[e[i].to] = dis[d] + e[i].len;
				q.push({ e[i].to,dis[e[i].to] });
			}
	}
}
```
下面就是几个相关的应用

# P1119 灾后重建
题目链接：灾后重建 - 洛谷

    这道题比较有意思的是村庄是先后修修好的，没修好的话就不能经过。一个村庄修好了，就意味着这个点可以通过了，那么是不是就可以利用这个点来松弛其他点之间的距离了，这个其实就是Floyd算法的应用。对于询问的每一天，把这一天之前所有修好的点都用来对其他所有点之间的最短路更新， 然后输出询问的点之间的距离。

    我一开始翻了个错误，就是在更新的时候只更新了修好的村庄，实际上这样是不对的，没修好的村庄只是没通车而已，在利用当前已经修好的村庄的条件下，没修好的村庄之间的最短路实际上已经发生改变了，只不过不能到达而已，需要注意一下。

    输出的时候记得判断一下是不是两个村庄可以到达，有一个没修好是不是可以通车的。

代码如下：
```cpp
#include<stdio.h>
#include<algorithm>
using namespace std;
#define Inf 0x3f3f3f3f
#define N 201
int dis[N][N];//存储任意点之间的距离
int flag[N];//标记当前村庄是否已经修复完毕
int n, m;

struct Village
{
	int t;//村庄修复时间
	int num;//村庄号
}v[N];
int now;//表示更新到的索引


int cmp(Village x, Village y)
{
	return x.t < y.t;
}

void update(int day)
{
	for (int i = now; i < n && v[i].t <= day; i++)
		flag[i] = 1;//修复好当前村庄
	while (v[now].t <= day && now<n)
	{
		int k = v[now].num;
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n ; j++)//两个点都修复好才更新距离
				dis[i][j] = min(dis[i][j], dis[i][k] + dis[k][j]);
		now++;
	}
}

int main()
{
	scanf("%d%d", &n, &m);
	for (int i = 0; i < n; i++)
	{
		scanf("%d", &v[i].t);
		v[i].num = i;
	}
	sort(v, v + n, cmp);//将村庄按照修复时间排序
	int x, y, l;
	for (int i = 0; i < n; i++)
		for (int j = 0; j < n; j++)
			dis[i][j] = Inf;
	for (int i = 0; i < n; i++)
		dis[i][i] = 0;//自己到自己是0
	for (int i = 1; i <= m; i++)
	{
		scanf("%d%d%d", &x, &y, &l);
		dis[x][y] = min(dis[x][y], l);
		dis[y][x] = dis[x][y];
	}
	int q; scanf("%d", &q);
	int day = 0;//表示当前更新到的天数
	update(day);
	int t;
	for (int i = 1; i<= q; i++ )
	{
		scanf("%d%d%d", &x, &y, &t);
		if (t > day)
		{
			day = t;//更新到t这一天
			update(day);
		}
		if (!flag[x]||!flag[y]||dis[x][y]==Inf)//没有路或者未修复
			printf("-1\n");
		else printf("%d\n", dis[x][y]);
	}
	return 0;
}
```
# P1629 邮递员送信
 题目链接：邮递员送信 - 洛谷

    这一题就是要计算第一个点到所有点的最短路，也要计算所有点到第一个点的最短路，第一个好算，第二个只要反向建图，那么问题也转化为了第一个点到所有点的最短距离，然后输出答案把两个相加即可。 

代码如下：
```cpp
#include<stdio.h>
#include<algorithm>
#include<queue>
using namespace std;
#define Inf 0x3f3f3f3f
#define N 1001
struct Edge
{
	int to,next;
	int len;
}e1[500001],e2[500001];
int last1[N], last2[N],cnt;
int dis1[N], dis2[N];
bool vis1[N], vis2[N];
int n, m;

struct Node
{
	int d, dis;
	inline bool operator <(const Node &x)const
	//重载运算符把最小的元素放在堆顶（大根堆）
	{
		return dis > x.dis;//这里注意符号要为''>''
	}
};
priority_queue<Node>q;

void add(Edge e[], int last[], int from, int to, int len)
{
	e[cnt].to = to;
	e[cnt].next = last[from];
	last[from] = cnt;
	e[cnt].len = len;
}

void dksa(int st, Edge e[], int last[], int dis[],bool vis[])
{
	dis[st] = 0;//重置不要忘记
	q.push({ st,0 });
	while (!q.empty())
	{
		Node node = q.top();
		q.pop();
		int d = node.d;
		if (!vis[d])
		{
			vis[d] = 1;
			for (int i = last[d]; i; i = e[i].next)
			{
				if (dis[e[i].to] > dis[d] + e[i].len)
				{
					dis[e[i].to] = dis[d] + e[i].len;
					q.push({ e[i].to ,dis[e[i].to] });
				}
			}
		}
	}
}

int main()
{
	scanf("%d%d", &n, &m);
	int x, y, len;
	for (int i = 1; i <= m; i++)
	{
		scanf("%d%d%d", &x, &y, &len);
		cnt++;
		add(e1, last1, x, y, len);
		add(e2, last2, y, x, len);//反向建图
	}
	for (int i = 1; i <= n; i++)
		dis1[i] = Inf,dis2[i] = Inf;
	dksa(1, e1, last1,dis1,vis1);
	dksa(1, e2, last2, dis2,vis2);
	int ans = 0;
	for (int i = 2; i <= n; i++)
		ans += dis1[i] + dis2[i];
	printf("%d\n", ans);
	return 0;
}
```
# P1144 最短路计数
 题目链接：最短路计数 - 洛谷

    这一题需要计算到达每个点的最短路的个数，一个点的最短路是由上一点传递而来，假设d[i]为i个点需要从1走至少d[i]步到达，那么d[i]=sum(d[j]==d[i]-1)，也就是这个点的上一个点的步数传递到这个点，把他们加起来就是这个点的最短路条数，这个过程的本质是bfs。

代码如下：
```cpp
#include<stdio.h>
#include<algorithm>
#include<vector>
#include<queue>
using namespace std;
#define mod 100003
#define N 1000001
vector<int>v[N];
//queue<int>q;
int que[N];
int quetmp[N];
int n, m;
int ans[N];
bool vis[N];
void bfs(int x)
{
	ans[x] = 1;
	vis[x] = 1;
	int num = 0;
	que[++num] = x;
	while (num)
	{
		for (int i = 1; i <= num; i++)
		{
			x = que[i];
			for (int i = 0; i < v[x].size(); i++)
				if (!vis[v[x][i]])//只给当前层的下一层传递,已经算过的点不会再算了,因为路径长度只会更长
					ans[v[x][i]] = (ans[v[x][i]] + ans[x]) % mod;
		}
		int num2 = 0;
		for (int i = 1; i <= num; i++)//整理接下来的队列
		{
			int x = que[i];
			for(int i=0;i<v[x].size();i++)
				if (!vis[v[x][i]])
				{
					quetmp[++num2] = v[x][i];
					vis[v[x][i]] = 1;
				}
		}
		num = num2;
		for (int i = 1; i <= num2; i++)
			que[i] = quetmp[i];
	}
}

int main()
{
	scanf("%d%d", &n, &m);
	int x, y;
	for (int i = 1; i <= m; i++)
	{
		scanf("%d%d", &x, &y);
		v[x].push_back(y);
		v[y].push_back(x);
	}
	bfs(1);
	for (int i = 1; i <= n; i++)
		printf("%d\n", ans[i]);
	return 0;
}
```
# P1462 通往奥格瑞玛的道路
题目链接：通往奥格瑞玛的道路 - 洛谷 

    这一题需要计算从起点1走到终点n需花费的最大值的最小值，乍一看非常绕。首先需要明确，通过一个城市需要交费，走一条路需要交扣血。

    考虑金额越大，那么能走的城市也越多，那么可选的路也越多，到达终点的可能性也越大，所以是有一个明显的单调性在这里的，因此二分金额，在计算最短路时不要经过不符合的城市，也就是收费高于二分结果的，把扣血看做是边权，看最少扣血也就是最短路是否小于血量，找到答案。

代码如下：
```cpp
#include<stdio.h>
#include<algorithm>
#include<queue>
using namespace std;
#define ll long long
#define N 10100
#define M 540000 //无向图要双倍数量!!!
#define Inf 0x7f7f7f7f
ll dis[N];//分别是1出发和n出发
bool vis[N];
int fee[N];

struct Edge
{
	int to, next;
	int len;
}e[M];
int last[N], cnt;
int n, m, b;

struct Node
{
	int d;
	ll dis;
	inline bool operator <(const Node &x)const
	//重载运算符把最小的元素放在堆顶（大根堆）
	{
		return dis > x.dis;//这里注意符号要为''>''
	}
};
priority_queue<Node>q;

void add(int from, int to, int len)
{
	e[++cnt].to = to;
	e[cnt].next = last[from];
	last[from] = cnt;
	e[cnt].len = len;
}

bool check(ll Max)
{
	for (int i = 1; i <= n; i++)
	{
		dis[i] = Inf;
		vis[i] = 0;
	}
	dis[1] = 0;
	q.push({ 1,0 });
	while (!q.empty())
	{
		Node x = q.top();
		int d = x.d;
		q.pop();
		if (vis[d])continue;
		vis[d] = 1;
		for (int i = last[d]; i; i = e[i].next)
		{
			if (fee[e[i].to] > Max)continue;//这个点不可以经过
			if (dis[e[i].to] > dis[d] + (ll)e[i].len)
			{
				dis[e[i].to] = dis[d] + (ll)e[i].len;
				q.push({ e[i].to,dis[d] + (ll)e[i].len});
			}
		}
	}
	if (dis[n] <= b)return 1;//计算最小血量能不能达到终点
	else return 0;
}

int main()
{
	scanf("%d%d%d", &n, &m, &b);
	int x, y, z;
	for (int i = 1; i <= n; i++)
		scanf("%d", fee + i);//每个城市的费用
	for (int i = 1; i <= m; i++)
	{
		scanf("%d%d%d", &x, &y,&z);
		add(x, y, z);
		add(y, x, z);
	}
	ll l = fee[1], r = Inf;
	while (l <= r)
	{
		ll mid = (l + r) / 2;
		if (check(mid))r = mid - 1;
		else l = mid + 1;
	}
	if (l == Inf + 1)printf("AFK\n");
	else printf("%lld\n", l);
	return 0;
}
```
# P1522 [USACO2.4] 牛的旅行 Cow Tours
 题目链接：[USACO2.4] 牛的旅行 Cow Tours - 洛谷   

    这道题数据量很小，可以承受比较高的复杂度，就是步骤比较繁琐，如下。

先走一遍dfs给不同牧场打上标记，划分区域，也就是染色操作。
骤跑一遍Floyd算出所有点之间的最短路，之后枚举计算同一牧场内的最大路最大值，作为每个牧场的直径。
因为要计算任意两个牧场连接后直径的最小值，所以枚举所有点的组合(i,j)，如果属于不同牧场，那么就把这两个点连起来看新牧场的直径，这个直径有两种可能性：一种是不经过新连的边，那么就是原来两个牧场的直径的较大值；如果经过新边，那么就是i所属牧场所有点到达i的最大距离加上j所属牧场到达j的最大距离加上i和j之间的距离。
在上面找边的过程中动态更新答案，最终输出结果。
代码如下：
```cpp
#include<stdio.h>
#include<algorithm>
#include<vector>
#include<math.h>
using namespace std;
#define Inf 0x3f3f3f3f
#define N 200
#define M N*N
double dis[N][N];//任意两点之间的最短距离
int color[N];//存储每个点所属的牧场类别
int colorNum;//记录牧场总数
double maxr[N];//记录每个牧场的直径
bool vis[N];
int n;

struct Edge
{
	int to, next;
	double len;
}e[M];
int last[N], cnt;

struct ZuoBiao
{
	int x, y;
}zuobiao[N];

double len(int x, int y)
{
	return sqrt(pow(zuobiao[x].x - zuobiao[y].x, 2) + pow(zuobiao[x].y - zuobiao[y].y, 2));
}

void add(int from, int to)
{
	e[++cnt].to = to;
	e[cnt].next = last[from];
	last[from] = cnt;
	e[cnt].len = sqrt(len(from, to));
}

void addColor(int x,int k)
{
	color[x] = k;
	for (int i = last[x]; i; i = e[i].next)
		if (!color[e[i].to])
			addColor(e[i].to, k);
}

int main()
{
	scanf("%d", &n);
	for (int i = 1; i <= n; i++)
		scanf("%d%d", &zuobiao[i].x, &zuobiao[i].y);
	for (int i = 1; i <= n; i++)
		for (int j = 1; j <= n; j++)
		{
			if (i == j)continue;
			dis[i][j] = Inf;//两点之间的最短距离
		}
	char ch;
	for (int i = 1; i <= n; i++)
		for (int j = 1; j <= n; j++)
		{
			scanf(" %c", &ch);
			if (ch == ''1'')
			{
				add(i, j);
				dis[i][j] = len(i, j);
			}
		}
	for (int i = 1; i <= n; i++)
		if (!color[i])
		{
			colorNum++ ;
			addColor(i,colorNum);//dfs染色
		}
	//计算最短路,同时记录每个牧场的直径
	for (int i = 1; i <= colorNum; i++)
		maxr[i] = 0;
	for (int k = 1; k <= n; k++)
		for (int i = 1; i <= n  ; i++)
			for (int j = 1; j <= n; j++)
			{
				if (i == j)continue;
				dis[i][j] = min(dis[i][j], dis[i][k] + dis[k][j]);
			}
	//记录每个牧场直径
	for (int i = 1; i <= n; i++)
		for (int j = 1; j <= n && j != i; j++)
			if (color[i] == color[j] && dis[i][j] != Inf)//同一色块并且有边
				maxr[color[i]] = max(maxr[color[i]], dis[i][j]);//如果是同一牧场那么更新直径
	double ans = Inf;//答案
	//枚举不同组合
	for(int i=1;i<=n;i++)
		for (int j = 1; j < i; j++)
		{
			if (color[i] == color[j])continue;//同一个牧场不考虑
			double ans1=0, ans2=0;//分别是第一个牧场经过i的最短路最大和第二个牧场经过j的最短路最大
			for (int k = 1; k <= n; k++)
			{
				if (color[k] == color[i] && dis[i][k] != Inf)
					ans1 = max(ans1, dis[i][k]);
				if (color[k] == color[j] && dis[j][k] != Inf)
					ans2 = max(ans2, dis[j][k]);
			}
			//考虑两种情况,不经过i,j,就是tmp,经过i,j,此时可能在i的也可能在j的
			double tmp = ans1 + ans2 + len(i, j);
			ans = min(ans,max(max(maxr[color[i]],maxr[color[j]]), tmp));
		}
	printf("%.6lf\n", ans);
	return 0;
}
```
 ❀❀❀完结撒花❀❀❀

​',0,'2023-01-03 20:31:03','2023-01-03 20:31:03');

INSERT INTO article_detail(article_id,version,content,deleted,create_time,update_time) VALUES(11,1,_utf8mb4'​

# 杂物
题目链接：杂务 - 洛谷 

    这一题需要计算最短的时间，利用了记忆化搜索的方式。将每一项工作抽象为图中的节点，所有前面需要完成的也就是节点的前驱，因为每一项工作完成所需要的最短总时长其实就是完成前面需要的工作的最早时长再加上这个工作的时长，因为所有工作的最早完成时间在一开始是未知的，所以需要先把所有前驱给计算出来， 所以搜索是需要先更新前驱结点，如果计算过了就直接返回结果，本质上也是拓扑排序，利用转移方程finalT[i]=min(finalT[...])+t[i]，也就是当前工作的时长加上最早前驱计算每个节点，确定最终答案。
```cpp
#include<stdio.h>
#include<algorithm>
#include<vector>
#define Max 100100
using namespace std;
vector<int>v1[Max];
int t[Max];//每个点的时间
int flag[Max];//标记每个点的访问情况
int finalT[Max];//标记最终的答案
int n;

int dfs(int x)
{
	if (flag[x])return finalT[x];
	for (int i = 0; i < v1[x].size(); i++)
		finalT[x] = max(finalT[x], finalT[v1[x][i]] + t[x]);
	flag[x] = 1;
	return finalT[x];
}

int main()
{
	scanf("%d", &n);
	int x, y;
	for (int i = 1; i <= n; i++)
	{
		scanf("%d%d", &x, t + i);
		while ( scanf("%d",&y) && y)//这样写更简洁
		{
			v1[x].push_back(y);//这个方笛一定要注意,给的形式是前驱
		}
	}
	int ans = 0;
	for (int i = 1; i <= n; i++)
		finalT[i] = t[i];
	for (int i = 1; i <= n; i++)
		ans = max(ans, dfs(i));
	printf("%d\n", ans);
	return 0;
}
```
#  最大食物链计数
题目链接：最大食物链计数 - 洛谷 

    这一题需要计算食物链的总数，将每个生物抽象为图中节点，一个生物吃另一个生物就是作为前驱。那么只有一个节点只有入度而没有出度才可以算作一条完整的食物链，对于每个节点，到达这个节点所有的食物链总数其实就是所有指向这个节点的食物链总和，也就是num[i]=∑num[j],j是i的前驱， 初始状态是将每个只有出度的点作为1，最后求和是对所有没有出度的求和。
```cpp
#include<stdio.h>
#include<algorithm>
#include<vector>
using namespace std;
#define ll long long
#define mod 80112002
vector<int>v[10000];
int n,m;
int flag[10000];
int vis[10000];//标记这个点有没有出度

int cal(int x)
{
	if (flag[x])return flag[x];
	if (v[x].size() == 0)
		return flag[x] = 1;//没有前驱就是1
	for (int i = 0; i < v[x].size(); i++)
	{
		flag[x] = (flag[x] + cal(v[x][i])) % mod;
	}
	return flag[x];
}

int main()
{
	scanf("%d%d", &n, &m);
	int x, y;
	for (int i = 1; i <= m; i++)
	{
		scanf("%d%d", &x, &y);
		vis[x] = 1;
		v[y].push_back(x);
	}
	int ans = 0;
	for (int i = 1; i <= n; i++)
	{
		if(!vis[i])
			ans = (ans + cal(i)) % mod;//cal计算所有前驱的和
	}
	printf("%d\n", ans);
	return 0;
}
```
# 排序
题目链接：排序 - 洛谷

    这道题因为有三种情况，也就是矛盾---出现环，无法排序---没有唯一拓扑序列，排序完成--有唯一的拓扑序列，恰好考察了拓扑排序的应用。这一题读入点要标记出现的点，在排序时候只要出现的点都有了那么排序就完成了，但是在排序中会出现几种情况：第一种是找了一轮都找不到度为0的点，那么这时候是出现矛盾了；第二种要是找了一轮找到了好几个度为0的那么就无法确定，一开始我是吃了这里的亏，也就是在函数中出现无法确定直接返回，但是由于无法确定还可能最终是矛盾的，所以不可以直接返回，最终返回是矛盾优先，因为直接就不用后面的判断了；第三种就是排序完成了，出现的字母个数也是等于总个数，那么就可以退出了。
```cpp
#include<stdio.h>
#include<algorithm>
#include<vector>
#include<queue>
using namespace std;
vector<int>v[200];
int du[200],du2[200];
char res[30];
bool exist[200];
bool flag[200];
queue<int>q;
int n, m;

int topo_sort(int cag)
{
	int cnt = 0;
	int not_sure = 0;
	for (int i = ''A''; i <= ''Z''; i++)
	{
		int first = 0;//标记是不是只有一个入队
		if (exist[i] && du2[i] == 0)
		{
			if (!first)first = 1;
			else not_sure = 1;
			q.push(i);
			res[++cnt] = i;
		}
	}
	if (q.empty())return -1;//矛盾
	while (!q.empty())
	{
		int x = q.front();
		q.pop();
		int first = 0;
		for (int i = 0; i < v[x].size(); i++)
		{
			du2[v[x][i]]--;
			if (!du2[v[x][i]])
			{
				if (!first)first = 1;
				else not_sure = 1;
				q.push(v[x][i]);
				res[++cnt] = v[x][i];
			}
		}
	}
	if (cnt != cag)return -1;//如果没排序完,那么就是有环,也就是矛盾
	if (not_sure)return 0;//不确定
	return 1;//排序完毕
}

int main()
{
	scanf("%d%d", &n, &m);
	char x, y;
	int cag = 0;//当前有的种类
	for (int i = 1; i <= m; i++)
	{
		scanf(" %c<%c", &x, &y);
		cag += (1-exist[x] + 1-exist[y]);
		exist[x] = 1;
		exist[y] = 1;
		v[x].push_back(y);
		du[y]++;
		for (int j = ''A''; j <= ''Z''; j++)
			du2[j] = du[j];
		int ans = topo_sort(cag);
		if (ans == -1)//矛盾
		{
			printf("Inconsistency found after %d relations.\n", i);
			return 0;
		}
		else if (cag==n && ans == 1)//排序完了
		{
			printf("Sorted sequence determined after %d relations: ", i, res);
			for (int j = 1; j <= n; j++) printf("%c", res[j]);
			printf(".\n");
			return 0;
		}
	}
	printf("Sorted sequence cannot be determined.\n");
	return 0;
}
```
# 车站分级
题目链接：[NOIP2013 普及组] 车站分级 - 洛谷

    这一题放在这里就是因为我觉得这一题就比较像是上一题排序的变形，从题目给出条件来看，车站经过的所有车站只要没有停，那么这个车站一定比停过的车站等级都要低，所以给出一个序列之后，比如1,3，设dj[i]为车站i的等级，那么就隐含了dj[1]>dj[2]，dj[3]>dj[2]，那么把所有的序列都转化为不等式，那么这一题就跟上一题一样了。需要注意的就是这一题需要统计的数量就是有多少层，每次找完一轮，度为0的都算作一层，相当于这些车站之间没有大小关系，但是跟不同层的是有前后关系的。
```cpp
#include<stdio.h>
#include<algorithm>
#include<queue>
#include<vector>
using namespace std;
#define N 1001
bool flag[N][N];//标记这条边flag[x][y]x是否已经建立和y的关系
vector<int>v[N];
int n, m,ans;
bool vis[N];
int du[N];
int Min = 0x3f3f3f, Max = -0x3f3f3f;
int a[N];//存储车道序列和后面暂时存放度0的点
void topo()
{
	//要计算有多少层,每一次找完一轮有几个度为0，那么这一次找到的都是算作一层
	//先存储起来，然后逐个减度
	int all_num=Max-Min+1;//这一轮找到的个数和总个数
	while (all_num)
	{
		int num = 0;
		//for (int i = Min; i <= Max; i++)
		for (int i = Min; i <= Max; i++)
		{
			if (!vis[i] && !du[i])
			{
				a[++num] = i;
				all_num--;
				vis[i] = 1;
			}
		}
		ans++;//一层
		for (int i = 1; i <= num; i++)
		{
			int x = a[i];
			for (int j = 0; j < v[x].size(); j++)
				du[v[x][j]]--;
		}
	}
}

int main()
{
	scanf("%d%d", &n, &m);
	int t, x;
	for (int i = 1; i <= m; i++)
	{
		scanf("%d", &t);
		for (int i = 1; i <= t; i++)
		{
			scanf("%d", a+i);
			vis[a[i]] = 1;
		}
		Min = min(Min, a[1]);
		Max = max(Max, a[t]);
		for (int i = a[1]; i <= a[t]; i++)
		{
			if (!vis[i])//表示没有出现过,这个点要比所有出现过的点要小
			{
				//建边
				for (int j = 1; j <= t; j++)
				{
					if (!flag[i][a[j]])//如果没有建过边
					{
						flag[i][a[j]] = 1;
						v[i].push_back(a[j]);
						du[a[j]]++;
					}
				}
			}
		}
		//一条车道建立完毕
		for (int i = a[1]; i <= a[t]; i++)
			vis[i] = 0;
	}
	topo();
	printf("%d\n", ans);
	return 0;
}
```cpp


 就先写到这么多吧。



​',0,'2023-01-01 21:20:30','2023-01-01 21:20:30');