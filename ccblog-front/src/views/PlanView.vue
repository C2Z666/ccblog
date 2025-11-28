<template>
   <HeaderBar />
  <div class="plan-view-container">
  
    <div class="plan-content">
      <!-- 时间轴切换 -->
      <div class="timeline-controls">
        <el-tabs v-model="activeTab" type="border-card" class="module-tabs">
          <el-tab-pane label="总体概览" name="overview">
            <div class="overview-stats">
              <el-row :gutter="20" justify="center">
                <el-col :span="6">
                  <div class="stat-card in-progress">
                    <div class="stat-number">{{totalModules-2}}</div>
                    <div class="stat-label">核心功能模块</div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="stat-card timeline">
                    <div class="stat-number">{{developmentDays}}</div>
                    <div class="stat-label">开发天数</div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="stat-card total-tasks">
                    <div class="stat-number">{{developmentTasks.length}}</div>
                    <div class="stat-label">总任务点</div>
                  </div>
                </el-col>
              </el-row>
            </div>
            
            <div class="overall-timeline">
              <div v-for="(month, index) in monthlyData" :key="index" class="month-section">
                <h3 class="month-title">{{month.name}}</h3>
                <div class="timeline-items">
                  <div 
                    v-for="(item, idx) in month.items" 
                    :key="idx" 
                    class="timeline-item"
                    @mouseenter="hoveredItem = item"
                    @mouseleave="hoveredItem = null"
                  >
                    <div class="timeline-dot"></div>
                    <div class="timeline-content">
                      <div class="timeline-date">{{item.date}}</div>
                      <div class="timeline-title">{{item.title}}</div>
                      <div class="timeline-module">{{item.module}}</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-tab-pane>
          
          <el-tab-pane 
            v-for="module in modules" 
            :key="module.id" 
            :label="module.name" 
            :name="module.id"
          >
            <div class="module-timeline">
              <div 
                v-for="(task, index) in getModuleTasks(module.id)" 
                :key="index" 
                class="task-item"
                :class="{ 'highlight': hoveredItem?.id === task.id }"
                @mouseenter="hoveredItem = task"
                @mouseleave="hoveredItem = null"
              >
                <div class="task-date">{{task.date}}</div>
                <div class="task-content">
                  <h4 class="task-title">{{task.title}}</h4>
                  <p class="task-description">{{task.description}}</p>
                </div>
                <div class="task-status">
                  <el-tag type="success" size="small">已完成</el-tag>
                </div>
              </div>
              
              <div v-if="getModuleTasks(module.id).length === 0" class="no-tasks">
                <el-empty description="暂无开发任务记录" />
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
      
      <!-- 悬浮详情卡片 -->
      <transition name="fade">
        <div v-if="hoveredItem" class="task-detail-card" :style="detailCardStyle">
          <h4>{{hoveredItem.title}}</h4>
          <p><strong>模块：</strong>{{hoveredItem.module}}</p>
          <p><strong>日期：</strong>{{hoveredItem.date}}</p>
          <p v-if="hoveredItem.description"><strong>描述：</strong>{{hoveredItem.description}}</p>
        </div>
      </transition>
    </div>
  <Footer class="custom-footer"></Footer>
  </div>
    <LoginDialog :clicked="loginDialogClicked"></LoginDialog>
  <RegisterDialog :clicked="registerDialogClicked"></RegisterDialog>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, provide } from 'vue';
import { ElTabs, ElTabPane, ElRow, ElCol, ElEmpty, ElTag } from 'element-plus';
import HeaderBar from '@/components/layout/HeaderBar.vue'
import Footer from '@/components/layout/Footer.vue'
import LoginDialog from '@/components/dialog/LoginDialog.vue'
import RegisterDialog from '@/components/dialog/RegisterDialog.vue'
// 定义开发任务接口
interface DevelopmentTask {
  id: string;
  title: string;
  description?: string;
  module: string;
  moduleId: string;
  date: string;
}

// 定义月份数据接口
interface MonthData {
  name: string;
  items: DevelopmentTask[];
}

// 定义模块接口
interface Module {
  id: string;
  name: string;
}

// 激活的标签页
const activeTab = ref('overview');

// 悬停的任务项
const hoveredItem = ref<DevelopmentTask | null>(null);

// 鼠标位置，用于定位详情卡片
const mousePosition = ref({ x: 0, y: 0 });

// 监听鼠标移动，用于定位详情卡片
const handleMouseMove = (event: MouseEvent) => {
  mousePosition.value = { x: event.clientX, y: event.clientY };
};

// 详情卡片样式
const detailCardStyle = computed(() => {
  return {
    left: `${mousePosition.value.x + 10}px`,
    top: `${mousePosition.value.y - 10}px`
  };
});

// 开发任务数据
const developmentTasks: DevelopmentTask[] = [
  // 前置准备
  { id: 'prep-1', title: '初始化swagger和扩展消息转换器', module: '前置准备', moduleId: 'preparation', date: '2025-09-24' },
  { id: 'prep-2', title: '连接数据库', description: '目前可以插入数据，但是数据库还没有确定', module: '前置准备', moduleId: 'preparation', date: '2025-09-24' },
  { id: 'prep-3', title: '拦截器开发', description: '每次访问都要执行的验证，还有个人数统计相关，合并到webConfig里面', module: '前置准备', moduleId: 'preparation', date: '2025-09-25' },
  { id: 'prep-4', title: '准备数据表', description: '记录在数据库设计.md', module: '前置准备', moduleId: 'preparation', date: '2025-09-25' },
  
  // 用户模块
  { id: 'user-1', title: '用户登录', module: '用户模块', moduleId: 'user', date: '2025-09-26' },
  { id: 'user-2', title: '异常捕获', module: '用户模块', moduleId: 'user', date: '2025-09-26' },
  { id: 'user-3', title: '用户注册', module: '用户模块', moduleId: 'user', date: '2025-09-27' },
  { id: 'user-4', title: '登出功能', description: '9.27开发，9.29前端测试通过', module: '用户模块', moduleId: 'user', date: '2025-09-27' },
  { id: 'user-5', title: '关注和取消关注', module: '用户模块', moduleId: 'user', date: '2025-10-01' },
  { id: 'user-6', title: '个人主页信息显示', module: '用户模块', moduleId: 'user', date: '2025-10-01' },
  { id: 'user-7', title: '获取用户关注列表', module: '用户模块', moduleId: 'user', date: '2025-10-01' },
  { id: 'user-8', title: '获取用户粉丝列表', module: '用户模块', moduleId: 'user', date: '2025-10-01' },
  { id: 'user-9', title: '用户关注/粉丝本地缓存', module: '用户模块', moduleId: 'user', date: '2025-10-17' },
  { id: 'user-10', title: '关注状态异步落库', module: '用户模块', moduleId: 'user', date: '2025-10-17' },
  { id: 'user-11', title: '用户信息本地缓存', module: '用户模块', moduleId: 'user', date: '2025-10-18' },
  { id: 'user-12', title: '关注id前端缓存', description: '前端持久存储', module: '用户模块', moduleId: 'user', date: '2025-10-18' },
  { id: 'user-13', title: '热门用户', module: '用户模块', moduleId: 'user', date: '2025-11-09' },
  
  // 文章模块
  { id: 'article-1', title: '新增文章', description: '9.27开发，9.29前端测试通过，10.02发布文章增加文章计数', module: '文章相关', moduleId: 'article', date: '2025-09-27' },
  { id: 'article-2', title: '查询所有类别和标签类', module: '文章相关', moduleId: 'article', date: '2025-09-29' },
  { id: 'article-3', title: '根据类别查询文章信息', description: '9.28完成，9.29前端测试通过，10.02改用一次sql+缓存完成查询，避免多次查表，最后改为一次插文章+缓存拼数据', module: '文章相关', moduleId: 'article', date: '2025-09-28' },
  { id: 'article-4', title: '根据用户id查询文章信息', module: '文章相关', moduleId: 'article', date: '2025-09-29' },
  { id: 'article-5', title: '文章详情页展示', description: '初步完成', module: '文章相关', moduleId: 'article', date: '2025-10-02' },
  { id: 'article-6', title: '文章点赞/收藏/取消点赞/取消收藏', description: '10.12完成异步落库更新', module: '文章相关', moduleId: 'article', date: '2025-10-03' },
  { id: 'article-7', title: '浏览记录', module: '文章相关', moduleId: 'article', date: '2025-10-04' },
  { id: 'article-8', title: '收藏记录', module: '文章相关', moduleId: 'article', date: '2025-10-04' },
  { id: 'article-9', title: '文章交互redis+rabbitmq异步落库', module: '文章相关', moduleId: 'article', date: '2025-10-10' },
  { id: 'article-10', title: '文章删除', module: '文章相关', moduleId: 'article', date: '2025-10-14' },
  { id: 'article-11', title: '文章编辑', module: '文章相关', moduleId: 'article', date: '2025-10-14' },
  { id: 'article-12', title: '文章详情本地缓存', module: '文章相关', moduleId: 'article', date: '2025-10-18' },
  { id: 'article-13', title: '根据标签获取文章', module: '文章相关', moduleId: 'article', date: '2025-11-08' },
  { id: 'article-14', title: '热门标签', module: '文章相关', moduleId: 'article', date: '2025-11-09' },
  { id: 'article-15', title: '上次编辑', module: '文章相关', moduleId: 'article', date: '2025-11-09' },
  { id: 'article-16', title: '文章搜索', module: '文章相关', moduleId: 'article', date: '2025-11-09' },
  
  // 评论模块
  { id: 'comment-1', title: '发布/回复评论', module: '评论模块', moduleId: 'comment', date: '2025-10-04' },
  { id: 'comment-2', title: '评论点赞/点踩', description: '需进一步完善', module: '评论模块', moduleId: 'comment', date: '2025-10-04' },
  { id: 'comment-3', title: '游标评论树', module: '评论模块', moduleId: 'comment', date: '2025-10-06' },
  { id: 'comment-4', title: '评论计数异步落库', module: '评论模块', moduleId: 'comment', date: '2025-10-12' },
  { id: 'comment-5', title: '评论交互redis+rabbitmq异步落库', module: '评论模块', moduleId: 'comment', date: '2025-10-12' },
  { id: 'comment-6', title: '删除评论', module: '评论模块', moduleId: 'comment', date: '2025-10-16' },
  { id: 'comment-7', title: '评论关注、粉丝列表本地缓存', module: '评论模块', moduleId: 'comment', date: '2025-10-17' },
  { id: 'comment-8', title: '关注对象前端缓存', module: '评论模块', moduleId: 'comment', date: '2025-10-18' },
  
  // 消息模块
  { id: 'notice-1', title: '多种消息存储', description: '采用异步落库', module: '消息模块', moduleId: 'notice', date: '2025-10-20' },
  { id: 'notice-2', title: '多种消息读取', module: '消息模块', moduleId: 'notice', date: '2025-10-20' },
  { id: 'notice-3', title: '前端直接关注/取关', module: '消息模块', moduleId: 'notice', date: '2025-10-20' },
  { id: 'notice-4', title: '评论跳转查看', module: '消息模块', moduleId: 'notice', date: '2025-10-21' },
  { id: 'notice-5', title: '一键清除通知', module: '消息模块', moduleId: 'notice', date: '2025-10-21' },
  { id: 'notice-6', title: '关注的人发布文章消息', module: '消息模块', moduleId: 'notice', date: '2025-10-21' },
  { id: 'notice-7', title: '私信消息预览', module: '消息模块', moduleId: 'notice', date: '2025-10-21' },
  
  // 聊天模块
  { id: 'chat-1', title: '获取用户聊天记录', module: '聊天模块', moduleId: 'chat', date: '2025-10-23' },
  { id: 'chat-2', title: '用户会话列表获取', module: '聊天模块', moduleId: 'chat', date: '2025-10-25' },
  { id: 'chat-3', title: '私信用户聊天', module: '聊天模块', moduleId: 'chat', date: '2025-10-25' },
  { id: 'chat-4', title: '后端生成会话级唯一递增seq', module: '聊天模块', moduleId: 'chat', date: '2025-10-27' },
  { id: 'chat-5', title: '撤回消息', module: '聊天模块', moduleId: 'chat', date: '2025-10-26' },
  { id: 'chat-6', title: '删除消息', module: '聊天模块', moduleId: 'chat', date: '2025-10-26' },
  { id: 'chat-7', title: '缓存', description: '只缓存最新一次消息', module: '聊天模块', moduleId: 'chat', date: '2025-10-30' },
  { id: 'chat-8', title: '会话删除/会话置顶', description: '严格来说是隐藏会话', module: '聊天模块', moduleId: 'chat', date: '2025-11-05' },
  { id: 'chat-9', title: '删除/重命名会话', module: '聊天模块', moduleId: 'chat', date: '2025-10-29' },
  { id: 'chat-10', title: '聊天记录存储', module: '聊天模块', moduleId: 'chat', date: '2025-10-29' },
  { id: 'chat-11', title: '获取AI会话', module: '聊天模块', moduleId: 'chat', date: '2025-10-28' },
  { id: 'chat-12', title: '获取AI聊天历史', module: '聊天模块', moduleId: 'chat', date: '2025-10-28' },
  { id: 'chat-13', title: 'SSE实现AI聊天', module: '聊天模块', moduleId: 'chat', date: '2025-10-28' },
  { id: 'chat-14', title: '清空全部已读状态', module: '聊天模块', moduleId: 'chat', date: '2025-11-10' },
  
  // 通用模块
  { id: 'common-2', title: '图片上传和展示', module: '通用模块', moduleId: 'common', date: '2025-11-06' },
  { id: 'common-3', title: '加入回复和消息数在角标', module: '通用模块', moduleId: 'common', date: '2025-11-07' },
  { id: 'common-4', title: '定时执行异步落库任务', module: '通用模块', moduleId: 'common', date: '2025-11-09' },

  // 优化和改善
  { id: 'optimize-1', title: '通过添加回复数砍掉评论的子查询是否有回复', module: '优化和改善', moduleId: 'optimize', date: '2025-10-30' },
  { id: 'optimize-2', title: '优化文章和评论查询', description: '移除连接查询用户信息，改为批量读redis', module: '优化和改善', moduleId: 'optimize', date: '2025-10-31' },
  { id: 'optimize-3', title: '修改用户信息界面', description: '需要输完全部才能保存，改为直接点出后查出信息填进去', module: '优化和改善', moduleId: 'optimize', date: '2025-10-31' },
  { id: 'optimize-4', title: '移除本地缓存', description: '本地缓存导致关注、粉丝列表滞后', module: '优化和改善', moduleId: 'optimize', date: '2025-10-30' },
  { id: 'optimize-5', title: '优化redis查询', description: '改为用redis更新相关数据传回', module: '优化和改善', moduleId: 'optimize', date: '2025-10-31' },
  { id: 'optimize-6', title: '抽象出hash redis批量读写的父类模板', module: '优化和改善', moduleId: 'optimize', date: '2025-10-31' },
  { id: 'optimize-7', title: '解决分页问题', description: '包括后端版本太低无法拦截和前端无法显示问题', module: '优化和改善', moduleId: 'optimize', date: '2025-10-31' },
  { id: 'optimize-8', title: '请求数据判断登录优化', module: '优化和改善', moduleId: 'optimize', date: '2025-11-01' },
  { id: 'optimize-9', title: '首页展示优化', description: '优化10000篇查询主页全部栏文章需要3s的问题', module: '优化和改善', moduleId: 'optimize', date: '2025-11-03' },
  { id: 'optimize-10', title: '优化主页查询', description: '改为了进入主页查询一次并且过滤掉无文章类别', module: '优化和改善', moduleId: 'optimize', date: '2025-11-03' },
  { id: 'optimize-11', title: '文章、用户、评论计数落库优化', description: '全部采用增量落库，避免失效导致重置问题', module: '优化和改善', moduleId: 'optimize', date: '2025-11-09' },
  { id: 'optimize-12', title: '文章交互优化', description: '文章不存在导航到空页', module: '优化和改善', moduleId: 'optimize', date: '2025-11-11' },
  { id: 'optimize-13', title: '更改mq队列的超时时长为无限', description: '避免未及时落库处理，不过理论上可能加一个合适时间+丢进私信队列后续处理可能更好', module: '优化和改善', moduleId: 'optimize', date: '2025-11-11' },
  { id: 'optimize-14', title: '解决并发多发多接收问题', description: '添加redisson锁防止重复发布和版本冲突', module: '优化和改善', moduleId: 'optimize', date: '2025-11-12' },
  { id: 'optimize-15', title: '优化interaction写入频繁问题', description: '减少重复发布和优化版本管理', module: '优化和改善', moduleId: 'optimize', date: '2025-11-12' },
  { id: 'optimize-16', title: '优化文章详情接口并发', description: '500并发下达到1000qps不出错', module: '优化和改善', moduleId: 'optimize', date: '2025-11-13' },
  { id: 'optimize-17', title: '优化后端详情接口', description: '分为两次查询，第一次查主要信息，第二次查状态、计数信息等', module: '优化和改善', moduleId: 'optimize', date: '2025-11-13' },
  { id: 'optimize-18', title: '文章计数落库引入分片架构', description: '大大减轻了锁的竞争，提高吞吐', module: '优化和改善', moduleId: 'optimize', date: '2025-11-13' },
  { id: 'optimize-19', title: 'Disruptor环形队列+批量分片聚合架构', description: '解决文章阅读计数更新的性能瓶颈问题，引入Disruptor环形队列减少通信时间，按articleId%SHARD分片解决锁竞争和聚合慢问题', module: '优化和改善', moduleId: 'optimize', date: '2025-11-14' },
  { id: 'optimize-20', title: '用户计数和状态同步优化', description: '同步优化了用户计数和用户状态，加入批量聚合，配合文章读事件更新', module: '优化和改善', moduleId: 'optimize', date: '2025-11-15' },
    ];

// 模块列表
const modules: Module[] = [
  { id: 'preparation', name: '前置准备' },
  { id: 'user', name: '用户模块' },
  { id: 'article', name: '文章模块' },
  { id: 'comment', name: '评论模块' },
  { id: 'notice', name: '消息模块' },
  { id: 'chat', name: '聊天模块' },
  { id: 'common', name: '通用模块' },
  { id: 'optimize', name: '优化和改善' },
];

// 计算属性
const completedTasks = computed(() => developmentTasks);
const totalModules = computed(() => modules.length);
const developmentDays = computed(() => {
  const startDate = new Date('2025-09-24');
  // 动态计算最后一个任务的日期
  const endDate = developmentTasks.length > 0 
    ? new Date(Math.max(...developmentTasks.map(task => new Date(task.date).getTime())))
    : new Date('2025-11-13'); // 提供默认值以防止空数组
  const diffTime = Math.abs(endDate.getTime() - startDate.getTime());
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  return diffDays;
});
const completionRate = computed(() => {
  // 这里简化计算，实际应根据总任务数计算
  return Math.round((developmentTasks.length / 100) * 100);
});

// 按月份分组的数据
const monthlyData = computed<MonthData[]>(() => {
  const monthMap = new Map<string, DevelopmentTask[]>();
  
  // 按日期排序任务
  const sortedTasks = [...developmentTasks].sort((a, b) => 
    new Date(a.date).getTime() - new Date(b.date).getTime()
  );
  
  // 按月份分组
  sortedTasks.forEach(task => {
    const date = new Date(task.date);
    const monthKey = `${date.getFullYear()}年${date.getMonth() + 1}月`;
    
    if (!monthMap.has(monthKey)) {
      monthMap.set(monthKey, []);
    }
    monthMap.get(monthKey)?.push(task);
  });
  
  // 转换为数组格式
  return Array.from(monthMap.entries()).map(([name, items]) => ({ name, items }));
});

// 根据模块ID获取任务
const getModuleTasks = (moduleId: string) => {
  return developmentTasks
    .filter(task => task.moduleId === moduleId)
    .sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime());
};

// 组件挂载时添加鼠标移动监听
onMounted(() => {
  document.addEventListener('mousemove', handleMouseMove);
  
  // 组件卸载时移除监听
  return () => {
    document.removeEventListener('mousemove', handleMouseMove);
  };
});

// 登录框
const changeClicked = () => {
  loginDialogClicked.value = !loginDialogClicked.value
  // console.log("clicked: ", loginDialogClicked.value)
}

provide('loginDialogClicked', changeClicked)
const loginDialogClicked = ref(false)

// 注册框
const changeRegisterClicked = () => {
  registerDialogClicked.value = !registerDialogClicked.value
  // console.log("clicked: ", registerDialogClicked.value)
}

provide('registerDialogClicked', changeRegisterClicked)
const registerDialogClicked = ref(false)
</script>

<style scoped>
.plan-view-container {
  min-height: 100vh;
  background: #ffffff;
  padding: 40px 0;
}

.plan-header {
  text-align: center;
  margin-bottom: 40px;
  color: white;
}

.plan-title {
  font-size: 36px;
  font-weight: 700;
  margin-bottom: 10px;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.plan-subtitle {
  font-size: 18px;
  opacity: 0.9;
  font-weight: 300;
}

.plan-content {
  max-width: 1200px;
  margin: 0 auto;
  background: white;
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  height: 90vh;
  overflow-y: auto;
  border: 1px solid #f0f0f0;
  position: relative;
}

.timeline-controls {
  background: white;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.module-tabs {
  --el-tabs-header-height: 60px;
}

.module-tabs .el-tabs__header {
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e7ed 100%);
  border-bottom: 1px solid #ebeef5;
}

.module-tabs .el-tabs__nav {
  height: 60px;
}

.module-tabs .el-tabs__item {
  height: 60px;
  line-height: 60px;
  padding: 0 24px;
  font-size: 16px;
  font-weight: 500;
  color: #606266;
  transition: all 0.3s ease;
}

.module-tabs .el-tabs__item:hover {
  color: #667eea;
}

.module-tabs .el-tabs__item.is-active {
  color: #667eea;
  font-weight: 600;
  background: white;
  box-shadow: 0 -2px 0 0 #667eea inset;
}

.module-tabs .el-tabs__content {
  padding: 30px;
}

/* 概览统计卡片 */
.overview-stats {
  margin-bottom: 30px;
}

.stat-card {
  padding: 25px;
  border-radius: 12px;
  text-align: center;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  position: relative;
  overflow: hidden;
}

.stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
}

.stat-card.completed {
  background: #f8f9fa;
}

.stat-card.completed::before {
  background: #1976d2;
}

.stat-card.in-progress {
  background: #f8f9fa;
}

.stat-card.in-progress::before {
  background: #1976d2;
}

.stat-card.timeline {
  background: #f8f9fa;
}

.stat-card.timeline::before {
  background: #1976d2;
}

.stat-card.progress {
  background: #f8f9fa;
}

.stat-card.progress::before {
  background: #1976d2;
}

.stat-card.total-tasks {
  background: #f8f9fa;
}

.stat-card.total-tasks::before {
  background: #1976d2;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
}

.stat-number {
  font-size: 32px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #606266;
}

/* 总体时间轴 */
.overall-timeline {
  position: relative;
}

.month-section {
  margin-bottom: 40px;
}

.month-title {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 20px;
  padding-left: 20px;
  border-left: 4px solid #1976d2;
}

.timeline-items {
  position: relative;
  padding-left: 30px;
}

.timeline-items::before {
  content: '';
  position: absolute;
  left: 8px;
  top: 0;
  bottom: 0;
  width: 2px;
  background: #e0e0e0;
}

.timeline-item {
  position: relative;
  padding-bottom: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  max-width: 95%;
}

.timeline-item:hover {
  transform: translateX(5px);
}

.timeline-date {
  font-size: 12px;
  color: #1976d2;
  font-weight: 500;
  margin-bottom: 5px;
  display: inline-block;
  padding: 2px 8px;
  background: rgba(25, 118, 210, 0.1);
  border-radius: 4px;
}

.timeline-dot {
  position: absolute;
  left: 0;
  top: 4px;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: #1976d2;
  box-shadow: 0 0 0 4px rgba(25, 118, 210, 0.2);
  transition: all 0.3s ease;
}

.timeline-item:hover .timeline-dot {
  background: #764ba2;
  transform: scale(1.2);
  box-shadow: 0 0 0 6px rgba(118, 75, 162, 0.2);
}

.timeline-content {
  padding: 15px 20px;
  background: #ffffff;
  border-radius: 8px;
  margin-left: 25px;
  margin-right: 20px;
  transition: all 0.3s ease;
  width: calc(100% - 45px);
  border-left: 3px solid transparent;
  border: 1px solid #f0f0f0;
}

.timeline-item:hover .timeline-content {
  background: #f8f9fa;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  border-left-color: #1976d2;
}

.timeline-item:hover .timeline-content {
  background: #ecf5ff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.timeline-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 5px;
}

.timeline-module {
  font-size: 14px;
  color: #667eea;
  font-weight: 500;
}

/* 模块任务列表 */
.module-timeline {
  max-height: 600px;
  overflow-y: auto;
  padding-right: 10px;
}

.task-item {
  display: flex;
  align-items: flex-start;
  padding: 20px;
  margin-bottom: 15px;
  background: #ffffff;
  border-radius: 12px;
  transition: all 0.3s ease;
  border-left: 4px solid transparent;
  border: 1px solid #f0f0f0;
}

.task-item:hover,
.task-item.highlight {
  background: #f8f9fa;
  transform: translateX(5px);
  border-left-color: #1976d2;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.task-date {
  flex: 0 0 100px;
  font-size: 14px;
  font-weight: 600;
  color: #1976d2;
  padding-right: 20px;
  text-align: right;
}

.task-content {
  flex: 1;
}

.task-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.task-description {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  margin: 0;
}

.task-status {
  flex: 0 0 80px;
  text-align: center;
}

.no-tasks {
  padding: 60px 20px;
  text-align: center;
}

/* 任务详情卡片 */
.task-detail-card {
  position: fixed;
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  z-index: 1000;
  min-width: 300px;
  max-width: 400px;
  border: 1px solid #ebeef5;
  transform: translate(-50%, -120%);
}

.task-detail-card h4 {
  margin: 0 0 15px 0;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

.task-detail-card p {
  margin: 8px 0;
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
}

/* 动画效果 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 滚动条样式 */
.module-timeline::-webkit-scrollbar {
  width: 6px;
}

.module-timeline::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.module-timeline::-webkit-scrollbar-thumb {
  background: #c0c4cc;
  border-radius: 3px;
}

.module-timeline::-webkit-scrollbar-thumb:hover {
  background: #909399;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .plan-title {
    font-size: 28px;
  }
  
  .plan-subtitle {
    font-size: 16px;
  }
  
  .module-tabs .el-tabs__content {
    padding: 20px;
  }
  
  .stat-card {
    margin-bottom: 15px;
  }
  
  .timeline-date {
    position: relative;
    left: 0;
    width: 100%;
    text-align: left;
    margin-bottom: 10px;
    padding-left: 25px;
  }
  
  .task-item {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .task-date {
    width: 100%;
    text-align: left;
    padding-right: 0;
    margin-bottom: 10px;
  }
  
  .task-status {
    width: 100%;
    text-align: left;
    margin-top: 10px;
  }
  
  .task-detail-card {
    position: fixed;
    left: 50% !important;
    top: 50% !important;
    transform: translate(-50%, -50%) !important;
    max-width: 90%;
  }
}
</style>