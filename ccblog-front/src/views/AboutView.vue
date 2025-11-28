<template>
  <HeaderBar></HeaderBar>
  <div class="home article-detail flex flex-col">
    <!-- 英雄区域 -->
    <div class="hero-section">
      <div class="hero-overlay"></div>
      <div class="hero-content">
        <div class="avatar-container">
          <div class="avatar-wrapper">
            <img
              :src="authorImg"
              alt="头像"
              class="avatar-image"
            />
            <div class="avatar-border"></div>
          </div>
        </div>
        <h1 class="hero-title">czc</h1>
        <p class="hero-subtitle">研一学生 | 开发爱好者 | AI爱好者</p>
        <div class="social-links">
          <a href="https://blog.csdn.net/weixin_60360239?spm=1000.2115.3001.5343" target="_blank" class="social-link csdn" title="CSDN主页">
            <el-icon><document /></el-icon>
            <span class="social-label">CSDN</span>
          </a>
          <a href="https://github.com/C2Z666?tab=repositories" target="_blank" class="social-link github" title="GitHub">
            <el-icon><Link /></el-icon>
            <span class="social-label">GitHub</span>
          </a>
          <a href="https://juejin.cn/user/1347316567260672" target="_blank" rel="noopener" class="social-link juejin" title="掘金主页">
            <el-icon><EditPen /></el-icon>
            <span class="social-label">掘金</span>
          </a>
        </div>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="about-content">
      <!-- 个人简介 -->
      <section class="about-section fade-in">
        <div class="section-header">
          <el-icon class="section-icon"><user /></el-icon>
          <h2 class="section-title">个人简介</h2>
        </div>
        <div class="personal-info">
          <div class="info-item">
            <span class="info-label">姓名</span>
            <span class="info-value">czc</span>
          </div>
          <div class="info-item">
            <span class="info-label">性别</span>
            <span class="info-value">男</span>
          </div>
          <div class="info-item">
            <span class="info-label">学历</span>
            <span class="info-value">研一</span>
          </div>
          <div class="info-item">
            <span class="info-label">联系方式</span>
            <span class="info-value">QQ: 1839156312</span>
          </div>
        </div>

        <div class="personal-bio">
          <p>本人是研一学生，热爱coding。拥有扎实的编程基础和良好的代码风格，
          对高性能系统设计、性能优化和人工智能领域有着浓厚的兴趣。</p>
            <p class="skill-description">
          熟悉多种编程语言，累计代码量超过数万行，具有面向对象开发思想，代码习惯良好。
          对高性能系统设计、缓存架构优化、性能优化有着浓厚兴趣。
        </p>
          <p>欢迎来找我交流技术、分享经验或 simply 聊天。</p>
        </div>
      </section>

      <!-- 技能特长 -->
      <section class="about-section fade-in">
        <div class="section-header">
          <el-icon class="section-icon"><cpu /></el-icon>
          <h2 class="section-title">技能特长</h2>
        </div>
        <div class="skill-category">
          <h3 class="category-title">编程语言</h3>
          <div class="skill-grid">
            <div class="skill-item">
              <div class="skill-name">Java</div>
              <div class="skill-progress">
                <div class="progress-bar" style="width: 85%"></div>
              </div>
            </div>
            <div class="skill-item">
              <div class="skill-name">C++</div>
              <div class="skill-progress">
                <div class="progress-bar" style="width: 80%"></div>
              </div>
            </div>
            <div class="skill-item">
              <div class="skill-name">Python</div>
              <div class="skill-progress">
                <div class="progress-bar" style="width: 80%"></div>
              </div>
            </div>
          </div>
        </div>
        <div class="skill-category">
          <h3 class="category-title">技术栈</h3>
          <div class="skill-tags">
            <div class="skill-tag">Linux</div>
            <div class="skill-tag">SQL</div>
            <div class="skill-tag">Redis</div>
            <div class="skill-tag">RabbitMQ</div>
            <div class="skill-tag">WebSocket</div>
            <div class="skill-tag">SSE</div>
            <div class="skill-tag">Caffeine</div>
            <div class="skill-tag">Vue.js</div>
            <div class="skill-tag">强化学习</div>
          </div>
        </div>
        <div class="skill-category">
          <h3 class="category-title">专业能力</h3>
          <div class="skill-tags">
            <div class="skill-tag">面向对象开发</div>
            <div class="skill-tag">并发系统设计</div>
            <div class="skill-tag">缓存架构优化</div>
            <div class="skill-tag">sim2sim迁移</div>
            <div class="skill-tag">强化学习训练</div>
          </div>
        </div>
      </section>

      <!-- 项目经历 -->
      <section class="about-section fade-in">
        <div class="section-header">
          <el-icon class="section-icon"><folder /></el-icon>
          <h2 class="section-title">项目经历</h2>
        </div>
        
        <!-- 项目1 -->
        <div class="project-container">
          <div class="project-timeline">
            <div class="timeline-dot"></div>
            <div class="timeline-line"></div>
          </div>
          <div class="project-card hover-lift">
            <div class="project-header">
              <h3 class="project-title">高性能博客系统</h3>
              <span class="project-period">2025.9.20 - 2025.11.13</span>
            </div>
            <div class="project-content">
              <div class="project-overview">
                <p>高性能博客系统，支持文章发布、评论、点赞、收藏等完整功能，通过<strong>多级缓存架构</strong>、<strong>异步批量处理机制</strong>，实现了高性能访问下的高性能。系统能够稳定处理大量用户同时在线的场景，模块耦合少，具备良好的可扩展性和稳定性。</p>
                <p class="mt-3">该项目核心亮点在于其<strong>高性能架构设计</strong>，通过<strong>缓存优化</strong>、<strong>消息队列解耦</strong>、<strong>异步批量处理</strong>和<strong>分片策略</strong>等技术，有效解决了大流量下的数据一致性和性能瓶颈问题。</p>
              </div>
              <div class="project-highlights">
                <h4 class="highlights-title">核心技术亮点</h4>
                <div class="feature-grid">
                  <div class="feature-item">
                    <div class="feature-icon">
                      <el-icon><sync /></el-icon>
                    </div>
                    <div class="feature-text"><strong>Redis + RabbitMQ双阈值聚合批量异步落库</strong></div>
                  </div>
                  <div class="feature-item">
                    <div class="feature-icon">
                    <el-icon><App /></el-icon>
                  </div>
                    <div class="feature-text"><strong>无限层级游标评论树</strong>，支持评论通知跳转并置顶展开</div>
                  </div>
                  <div class="feature-item">
                    <div class="feature-icon">
                    <el-icon><Message /></el-icon>
                  </div>
                    <div class="feature-text"><strong>WebSocket实时聊天</strong>，支持撤回/删除/置顶</div>
                  </div>
                  <div class="feature-item">
                    <div class="feature-icon">
                      <el-icon><robot /></el-icon>
                    </div>
                    <div class="feature-text"><strong>SSE通信调用大模型AI聊天功能</strong></div>
                  </div>
                  <div class="feature-item">
                    <div class="feature-icon">
                      <el-icon><box /></el-icon>
                    </div>
                    <div class="feature-text"><strong>Caffeine + Redis双层缓存架构，缓存索引保证双写一致性</strong></div>
                  </div>
                  <div class="feature-item">
                    <div class="feature-icon">
                      <el-icon><checked /></el-icon>
                    </div>
                    <div class="feature-text"><strong>Disruptor环形队列+批量分片聚合架构</strong>，大幅提升高性能下读写性</div>
                  </div>
          
      
                </div>
              </div>
              <div class="performance-metrics">
                <h4 class="metrics-title">性能测试结果(10000篇文章和1000个用户)</h4>
                <div class="metrics-grid">
                  <div class="metric-card">
                    <div class="metric-label">测试接口</div>
                    <div class="metric-value">文章详情</div>
                    <div class="metric-desc">内含阅读时间落库、reids更新操作</div>
                  </div>
                  <div class="metric-card">
                    <div class="metric-label">处理请求数</div>
                    <div class="metric-value">490,474</div>
                    <div class="metric-desc">2min内HTTP请求总数</div>
                  </div>
                  <div class="metric-card">
                    <div class="metric-label">系统吞吐量</div>
                    <div class="metric-value">4000 QPS</div>
                    <div class="metric-desc">500并发下的实际吞吐量</div>
                  </div>
                  <div class="metric-card">
                    <div class="metric-label">错误率</div>
                    <div class="metric-value">0.00%</div>
                    <div class="metric-desc">500并发下的稳定性</div>
                  </div>
                  <div class="metric-card">
                    <div class="metric-label">平均响应时间</div>
                    <div class="metric-value">88ms</div>
                    <div class="metric-desc">500并发下的平均响应</div>
                  </div>
                  <div class="metric-card">
                    <div class="metric-label">响应时间中位数</div>
                    <div class="metric-value">87ms</div>
                    <div class="metric-desc">500并发下的响应时间中位数</div>
                  </div>
                  <div class="metric-card">
                    <div class="metric-label">90%响应时间</div>
                    <div class="metric-value">159ms</div>
                    <div class="metric-desc">500并发下的90%响应</div>
                  </div>
                  <div class="metric-card">
                    <div class="metric-label">99%响应时间</div>
                    <div class="metric-value">212ms</div>
                    <div class="metric-desc">500并发下的99%响应</div>
                  </div>
      
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 项目2 -->
        <div class="project-container">
          <div class="project-timeline">
            <div class="timeline-dot"></div>
            <div class="timeline-line"></div>
          </div>
          <div class="project-card hover-lift">
            <div class="project-header">
              <h3 class="project-title">人形机器人仿真项目</h3>
              <span class="project-period">2024.11 - 2025.5</span>
            </div>
            <div class="project-content">
              <div class="project-overview">
                <p>在实习过程中负责的人形机器人仿真项目，专注于强化学习训练和环境迁移。项目实现了从Issac Gym到MuJoCo的sim2sim迁移，通过强化学习算法训练机器人稳定行走，并探索了sim2real部署方案。</p>
              </div>
              <div class="feature-grid">
                <div class="feature-item">
                  <div class="feature-icon">
                    <el-icon><cpu /></el-icon>
                  </div>
                  <div class="feature-text"><strong>Issac Gym高性能并行仿真环境</strong>配置与优化</div>
                </div>
                <div class="feature-item">
                  <div class="feature-icon">
                    <el-icon><box /></el-icon>
                  </div>
                  <div class="feature-text">基于<strong>PPO算法的强化学习策略设计</strong>，在仿真中实现机器人稳定自主行走功能</div>
                </div>
                <div class="feature-item">
                  <div class="feature-icon">
                    <el-icon><sync /></el-icon>
                  </div>
                  <div class="feature-text"><strong>MuJoCo环境sim2sim迁移</strong></div>
                </div>
                <div class="feature-item">
                  <div class="feature-icon">
                    <el-icon><connection /></el-icon>
                  </div>
                  <div class="feature-text"><strong>sim2real部署框架</strong></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
    <Footer></Footer>
  </div>
  <LoginDialog :clicked="loginDialogClicked"></LoginDialog>
</template>


<script setup lang="ts">
import HeaderBar from '@/components/layout/HeaderBar.vue'
import Footer from '@/components/layout/Footer.vue'
import authorImg from '@/assets/static/img/author.jpg?url'
import LoginDialog from '@/components/dialog/LoginDialog.vue'
import {onMounted, onUnmounted, provide, ref} from 'vue'
import { Document, Link, InfoFilled, Folder, User, Cpu, Connection, Message, Avatar as Robot, Box, Check as Checked, Promotion as App, Refresh as Sync, ChatDotRound, EditPen } from '@element-plus/icons-vue'


// 登录框
const changeClicked = () => {
  loginDialogClicked.value = !loginDialogClicked.value
  console.log("clicked: ", loginDialogClicked.value)
}

provide('loginDialogClicked', changeClicked)
const loginDialogClicked = ref(false)

// 监听滚动，添加动画效果
const handleScroll = () => {
  const sections = document.querySelectorAll('.fade-in')
  sections.forEach(section => {
    const sectionTop = section.getBoundingClientRect().top
    const sectionBottom = section.getBoundingClientRect().bottom
    const isVisible = (sectionTop < window.innerHeight - 100) && (sectionBottom > 0)
    
    if (isVisible) {
      section.classList.add('visible')
    }
  })
}

onMounted(() => {
  // 初始检查可见元素
  handleScroll()
  // 添加滚动监听
  window.addEventListener('scroll', handleScroll)
})

// 组件卸载时移除监听
onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
/* 基础样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

/* 英雄区域 */
.hero-section {
  position: relative;
  height: 70vh;
  min-height: 500px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  overflow: hidden;
  margin-bottom: 60px;
}

.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, rgba(0,0,0,0.1) 100%);
}

.hero-content {
  position: relative;
  text-align: center;
  color: white;
  z-index: 1;
  animation: fadeIn 1.5s ease-out;
}

.avatar-container {
  margin-bottom: 30px;
}

.avatar-wrapper {
  position: relative;
  width: 180px;
  height: 180px;
  margin: 0 auto;
}

.avatar-image {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  border: 4px solid rgba(255, 255, 255, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 3rem;
  color: white;
  position: relative;
  z-index: 2;
}

.avatar-border {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: linear-gradient(45deg, #ff6b6b, #feca57, #ff9ff3, #54a0ff);
  animation: rotate 6s linear infinite;
  z-index: 1;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.hero-title {
  font-size: 3.5rem;
  font-weight: bold;
  margin-bottom: 16px;
  letter-spacing: 1px;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
  animation: slideUp 0.8s ease-out 0.2s both;
}

.hero-subtitle {
  font-size: 1.25rem;
  margin-bottom: 30px;
  opacity: 0.9;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
  animation: slideUp 0.8s ease-out 0.4s both;
}

.social-links {
  display: flex;
  justify-content: center;
  gap: 16px;
  animation: slideUp 0.8s ease-out 0.6s both;
}

.social-link {
  min-width: 48px;
  height: 48px;
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.25rem;
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
  cursor: pointer;
  border: 2px solid rgba(255, 255, 255, 0.3);
  padding: 0 16px;
  text-decoration: none;
}

.social-label {
  margin-left: 8px;
  font-size: 0.9rem;
  font-weight: 500;
  white-space: nowrap;
}

.social-link:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-4px) scale(1.1);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
}

.social-link.github:hover {
  background: #333;
  border-color: #333;
}

.social-link.csdn:hover {
  background: #ff6b00;
  border-color: #ff6b00;
}

.social-link.juejin:hover {
  background: #e6f7ff;
  border-color: #1890ff;
  color: #1890ff;
}

/* 主要内容区域 */
.about-content {
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
  padding: 0 20px 60px;
  display: flex;
  flex-direction: column;
  gap: 50px;
}

/* 通用区块样式 */
.about-section {
  background: #ffffff;
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
  border: 1px solid rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  opacity: 0;
  transform: translateY(30px);
}

.about-section.visible {
  opacity: 1;
  transform: translateY(0);
}

.fade-in {
  transition: opacity 0.8s ease, transform 0.8s ease;
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 2px solid #f0f0f0;
}

.section-icon {
  font-size: 1.5rem;
  color: #409eff;
  margin-right: 12px;
  background: #f0f9ff;
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.section-title {
  font-size: 2rem;
  font-weight: bold;
  color: #303133;
  margin: 0;
}

/* 个人信息 */
.personal-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background: #fafafa;
  border-radius: 10px;
  transition: all 0.3s ease;
}

.info-item:hover {
  background: #f0f9ff;
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
}

.info-label {
  font-weight: 600;
  color: #606266;
}

.info-value {
  color: #303133;
  font-weight: 500;
}

.personal-bio {
  color: #606266;
  line-height: 1.8;
  font-size: 1.1rem;
}

.personal-bio p {
  margin-bottom: 16px;
}

.personal-bio p:last-child {
  margin-bottom: 0;
}

/* 技能部分 */
.skill-category {
  margin-bottom: 30px;
}

.skill-category:last-child {
  margin-bottom: 20px;
}

.category-title {
  font-size: 1.3rem;
  font-weight: 600;
  color: #303133;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid #f0f0f0;
}

.skill-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
}

.skill-item {
  background: #fafafa;
  padding: 20px;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.skill-item:hover {
  background: #f0f9ff;
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
}

.skill-name {
  font-weight: 600;
  color: #303133;
  margin-bottom: 12px;
  display: flex;
  justify-content: space-between;
}

.skill-progress {
  height: 8px;
  background: rgba(64, 158, 255, 0.2);
  border-radius: 4px;
  overflow: hidden;
}

.progress-bar {
  height: 100%;
  background: linear-gradient(90deg, #409eff, #66b1ff);
  border-radius: 4px;
  transition: width 1.5s ease-in-out;
}

.skill-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.skill-tag {
  background: linear-gradient(135deg, #667eea20 0%, #764ba220 100%);
  color: #667eea;
  padding: 8px 18px;
  border-radius: 20px;
  font-size: 0.95rem;
  font-weight: 500;
  transition: all 0.3s ease;
  border: 1px solid rgba(102, 126, 234, 0.2);
}

.skill-tag:hover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.skill-description {
  color: #606266;
  line-height: 1.8;
  font-size: 1.05rem;
  margin-top: 20px;
}

/* 项目经历 */
.project-container {
  display: flex;
  margin-bottom: 40px;
}

.project-container:last-child {
  margin-bottom: 0;
}

.project-timeline {
  position: relative;
  margin-right: 30px;
  display: flex;
  flex-direction: column;
  align-items: center;
  flex-shrink: 0;
}

.timeline-dot {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: linear-gradient(135deg, #409eff, #66b1ff);
  border: 4px solid white;
  box-shadow: 0 2px 10px rgba(64, 158, 255, 0.3);
  z-index: 2;
}

.timeline-line {
  width: 2px;
  background: linear-gradient(180deg, #409eff, #66b1ff);
  flex: 1;
  margin-top: 10px;
  opacity: 0.3;
}

.project-card {
  flex: 1;
  background: #ffffff;
  border-radius: 16px;
  padding: 30px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
  border: 1px solid rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.project-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: linear-gradient(180deg, #409eff, #66b1ff);
}

.hover-lift:hover {
  transform: translateY(-8px);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
}

.project-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 15px;
}

.project-title {
  font-size: 1.5rem;
  font-weight: bold;
  color: #303133;
  margin: 0;
}

.project-period {
  color: #409eff;
  font-size: 0.9rem;
  background: #ecf5ff;
  padding: 6px 16px;
  border-radius: 20px;
  font-weight: 500;
}

.project-overview {
  margin-bottom: 25px;
}

.project-overview p {
  color: #606266;
  line-height: 1.8;
  font-size: 1.05rem;
}

.project-highlights {
  margin-bottom: 25px;
}

.highlights-title,
.metrics-title {
  font-size: 1.2rem;
  font-weight: 600;
  color: #303133;
  margin-bottom: 20px;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}

.feature-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 10px;
  transition: all 0.3s ease;
}

.feature-item:hover {
  background: #f0f9ff;
  transform: translateY(-2px);
}

.feature-icon {
  font-size: 1.25rem;
  color: #409eff;
  background: white;
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  flex-shrink: 0;
}

.feature-text {
  font-size: 0.95rem;
  color: #606266;
  line-height: 1.6;
}

.performance-metrics {
  background: linear-gradient(135deg, #f0f9ff 0%, #e6f7ff 100%);
  padding: 25px;
  border-radius: 12px;
  border: 1px solid #d9ecff;
}

.metrics-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.metric-card {
  background: white;
  padding: 20px;
  border-radius: 10px;
  text-align: center;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
}

.metric-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.metric-label {
  font-size: 0.9rem;
  color: #606266;
  margin-bottom: 8px;
}

.metric-value {
  font-size: 1.8rem;
  font-weight: bold;
  color: #f56c6c;
  margin-bottom: 4px;
}

.metric-desc {
  font-size: 0.85rem;
  color: #909399;
}

/* 动画效果 */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-section {
    height: 60vh;
    min-height: 400px;
  }
  
  .avatar-wrapper {
    width: 140px;
    height: 140px;
  }
  
  .hero-title {
    font-size: 2.5rem;
  }
  
  .hero-subtitle {
    font-size: 1.1rem;
  }
  
  .social-link {
    width: 42px;
    height: 42px;
    font-size: 1.1rem;
  }
  
  .about-content {
    padding: 0 16px 40px;
    gap: 30px;
  }
  
  .about-section {
    padding: 25px;
  }
  
  .section-title {
    font-size: 1.6rem;
  }
  
  .project-container {
    flex-direction: column;
  }
  
  .project-timeline {
    margin-right: 0;
    margin-bottom: 20px;
    flex-direction: row;
  }
  
  .timeline-dot {
    margin-right: 20px;
  }
  
  .timeline-line {
    width: 100%;
    height: 2px;
    margin-top: 0;
    margin-right: 20px;
  }
  
  .project-card {
    padding: 20px;
  }
  
  .personal-info {
    grid-template-columns: 1fr;
  }
  
  .skill-grid,
  .feature-grid,
  .metrics-grid {
    grid-template-columns: 1fr;
  }
  
  .project-header {
    flex-direction: column;
    align-items: flex-start;
  }
}

@media (max-width: 480px) {
  .hero-title {
    font-size: 2rem;
  }
  
  .about-section {
    padding: 20px;
  }
  
  .section-title {
    font-size: 1.4rem;
  }
}
</style>
