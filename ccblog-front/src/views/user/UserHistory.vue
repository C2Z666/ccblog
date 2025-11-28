<template>
  <!-- 创作历程时间线组件 -->
  <div class="timeline-container">
    <div class="timeline-header">
      <h3 class="timeline-title">创作历程</h3>
      <div class="timeline-subtitle">记录每一步成长</div>
    </div>
    
    <div v-if="user.yearArticleList && user.yearArticleList.length > 0" class="timeline-wrapper">
      <!-- 时间线垂直主干 -->
      <div class="timeline-line"></div>
      
      <!-- 时间线节点 -->
      <div 
        v-for="(year, index) in sortedYearList" 
        :key="year.year"
        class="timeline-item"
        :style="{ animationDelay: `${index * 150}ms` }"
      >
        <!-- 时间点标记 -->
        <div class="timeline-marker">
          <div class="timeline-dot"></div>
        </div>
        
        <!-- 时间内容卡片 -->
        <div class="timeline-content">
          <div class="timeline-year">{{ year.year }}</div>
          <div class="timeline-stats">
            <div class="stat-item">
              <span class="stat-number">{{ year.articleCount }}</span>
              <span class="stat-label">文章</span>
            </div>
          </div>
          <div class="timeline-description">
            共发表文章 {{ year.articleCount }} 篇
          </div>
        </div>
      </div>
    </div>
    
    <!-- 空状态 -->
    <div v-else class="empty-timeline">
      <div class="empty-icon">📝</div>
      <div class="empty-title">暂未发布任何内容</div>
      <!-- <div class="empty-subtitle">开始您的创作之旅吧！</div> -->
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { UserBaseInfo } from '@/http/ResponseTypes/UserInfoType/UserStatisticInfoType'

// 定义组件属性
const props = defineProps<{
  user: UserBaseInfo
}>()

// 计算属性：按年份倒序排序
const sortedYearList = computed(() => {
  if (!props.user.yearArticleList) return []
  return [...props.user.yearArticleList].sort((a, b) => 
    Number(b.year) - Number(a.year) // 从新到旧排序
  )
})
</script>

<style scoped>
/* 时间线容器基础样式 */
.timeline-container {
  background: linear-gradient(145deg, #ffffff, #f8f9fa);
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.5);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.timeline-container:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.12);
}

/* 头部样式 */
.timeline-header {
  margin-bottom: 32px;
  text-align: center;
}

.timeline-title {
  font-size: 24px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 8px 0;
  position: relative;
  display: inline-block;
}

.timeline-title::after {
  content: '';
  position: absolute;
  bottom: -12px;
  left: 50%;
  transform: translateX(-50%);
  width: 40px;
  height: 3px;
  background: linear-gradient(90deg, #4e7cff, #5797ff);
  border-radius: 3px;
  transition: width 0.3s ease;
}

.timeline-container:hover .timeline-title::after {
  width: 60px;
}

.timeline-subtitle {
  font-size: 14px;
  color: #64748b;
  margin-top: 16px;
}

/* 时间线包装器 */
.timeline-wrapper {
  position: relative;
  padding-left: 32px;
  max-width: 800px;
  margin: 0 auto;
}

/* 时间线垂直线 */
.timeline-line {
  position: absolute;
  left: 8px;
  top: 0;
  bottom: 0;
  width: 2px;
  background: linear-gradient(to bottom, #4e7cff, #e0e7ff);
  transform: translateX(10px);
}

/* 时间线项 */
.timeline-item {
  position: relative;
  margin-bottom: 32px;
  opacity: 0;
  transform: translateY(20px);
  animation: fadeInUp 0.6s ease forwards;
}

/* 最后一项去除下边距 */
.timeline-item:last-child {
  margin-bottom: 0;
}

/* 动画关键帧 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 时间点标记 */
.timeline-marker {
  position: absolute;
  left: -32px;
  top: 0;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;
}

.timeline-dot {
  width: 16px;
  height: 16px;
  background: #ffffff;
  border: 3px solid #4e7cff;
  border-radius: 50%;
  box-shadow: 0 4px 12px rgba(78, 124, 255, 0.3);
  transition: all 0.3s ease;
}

.timeline-item:hover .timeline-dot {
  transform: scale(1.2);
  box-shadow: 0 6px 20px rgba(78, 124, 255, 0.4);
}

/* 时间线内容卡片 */
.timeline-content {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  padding: 20px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
  cursor: pointer;
}

.timeline-content:hover {
  transform: translateX(8px);
  background: rgba(78, 124, 255, 0.05);
  border-color: rgba(78, 124, 255, 0.2);
  box-shadow: 0 8px 24px rgba(78, 124, 255, 0.15);
}

/* 年份显示 */
.timeline-year {
  font-size: 18px;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 12px;
}

/* 统计信息 */
.timeline-stats {
  margin-bottom: 12px;
}

.stat-item {
  display: inline-flex;
  align-items: baseline;
  gap: 6px;
}

.stat-number {
  font-size: 28px;
  font-weight: 700;
  color: #4e7cff;
  transition: transform 0.3s ease;
}

.timeline-item:hover .stat-number {
  transform: scale(1.05);
}

.stat-label {
  font-size: 14px;
  color: #64748b;
  font-weight: 500;
}

/* 描述文本 */
.timeline-description {
  font-size: 14px;
  color: #64748b;
  line-height: 1.6;
}

/* 空状态样式 */
.empty-timeline {
  text-align: center;
  padding: 60px 20px;
  color: #64748b;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
  opacity: 0.8;
}

.empty-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #2c3e50;
}

.empty-subtitle {
  font-size: 14px;
  color: #94a3b8;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .timeline-container {
    padding: 16px;
  }
  
  .timeline-title {
    font-size: 20px;
  }
  
  .timeline-wrapper {
    padding-left: 24px;
  }
  
  .timeline-line {
    left: 4px;
    transform: translateX(8px);
  }
  
  .timeline-marker {
    left: -24px;
  }
  
  .timeline-dot {
    width: 14px;
    height: 14px;
    border-width: 2px;
  }
  
  .timeline-content {
    padding: 16px;
  }
  
  .stat-number {
    font-size: 24px;
  }
  
  .empty-timeline {
    padding: 40px 16px;
  }
}

@media (max-width: 480px) {
  .timeline-item {
    margin-bottom: 24px;
  }
  
  .timeline-content {
    padding: 14px;
  }
  
  .timeline-year {
    font-size: 16px;
  }
  
  .stat-number {
    font-size: 20px;
  }
}
</style>
