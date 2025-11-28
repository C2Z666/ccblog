<template>
  <footer class="footer-wrap">
    <div class="foot-container">
      <!-- 站点信息部分 -->
      <div class="site-info">
        <div class="logo-area">
          <h3 class="site-title">ccblog</h3>
          <p class="site-subtitle">让创意连接你我</p>
        </div>
        <div class="link-area">
          <a class="link-item" href="https://beian.miit.gov.cn" target="_blank" rel="noopener noreferrer">
            {{ global.siteInfo.websiteRecord }}
          </a>
          <router-link class="link-item" to="/about">
            czc
          </router-link>
        </div>
      </div>
      
      <!-- 统计信息部分 -->
      <!-- <div class="stats-container">
        <div class="stats-section">
          <h4 class="stats-title">全站统计(敬请期待)</h4>
          <div class="stats-grid">
            <div class="stat-item">
              <span class="stat-label">总 PV</span>
              <span class="stat-value">{{ global.siteStatisticInfo.pv || 0 }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">总 UV</span>
              <span class="stat-value">{{ global.siteStatisticInfo.uv || 0 }}</span>
            </div>
          </div>
        </div>
        
        <div class="stats-section">
          <h4 class="stats-title">今日统计(敬请期待)</h4>
          <div class="stats-grid">
            <div class="stat-item">
              <span class="stat-label">今日 PV</span>
              <span class="stat-value">{{ global.todaySiteStatisticInfo.pv || 0 }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">今日 UV</span>
              <span class="stat-value">{{ global.todaySiteStatisticInfo.uv || 0 }}</span>
            </div>
            <div class="stat-item online-count">
              <span class="stat-label">当前在线</span>
              <span class="stat-value online">{{ global.onlineCnt || 0 }}</span>
            </div>
          </div>
        </div>
      </div> -->
      
      <!-- 版权信息 -->
      <div class="copyright">
        <p>© {{ '2024-' + format(new Date(), 'yyyy') }} ccblog 版权所有</p>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
import { format } from 'date-fns'
import { useGlobalStore } from '@/stores/global'
const globalStore = useGlobalStore()
const global = globalStore.global
</script>

<style scoped>
.footer-wrap {
  width: 100%;
  height: auto;
  min-height: 60px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 1rem;
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.1);
}

.foot-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

/* 站点信息部分 */
.site-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
}

.logo-area {
  display: flex;
  flex-direction: column;
  gap: 0.1rem;
}

.site-title {
  margin: 0;
  font-size: 1.1rem;
  font-weight: 600;
  letter-spacing: 0.5px;
}

.site-subtitle {
  margin: 0;
  font-size: 0.75rem;
  opacity: 0.9;
}

.link-area {
  display: flex;
  gap: 0.75rem;
  flex-wrap: wrap;
}

.link-item {
  color: white;
  text-decoration: none;
  padding: 0.25rem 0.75rem;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  transition: all 0.2s ease;
  font-size: 0.75rem;
}

.link-item:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-1px);
}

/* 统计信息部分 */
.stats-container {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  gap: 1rem;
  padding: 0.5rem 0;
  border-top: 1px solid rgba(255, 255, 255, 0.15);
  border-bottom: 1px solid rgba(255, 255, 255, 0.15);
}

.stats-section {
  flex: 1;
  min-width: 150px;
}

.stats-title {
  margin: 0 0 0.5rem 0;
  font-size: 0.8rem;
  font-weight: 500;
  text-align: center;
  opacity: 0.9;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(80px, 1fr));
  gap: 0.5rem;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.15rem;
  padding: 0.5rem;
  background: rgba(255, 255, 255, 0.06);
  border-radius: 6px;
  transition: transform 0.2s ease;
}

.stat-item:hover {
  transform: translateY(-2px);
  background: rgba(255, 255, 255, 0.1);
}

.stat-label {
  font-size: 0.65rem;
  opacity: 0.8;
}

.stat-value {
  font-size: 1rem;
  font-weight: 600;
}

.stat-value.online {
  color: #4ade80;
  animation: pulse 2s infinite;
}

/* 版权信息 */
.copyright {
  text-align: center;
  padding-top: 0.5rem;
  font-size: 0.7rem;
  opacity: 0.8;
  line-height: 1.2;
}

/* 动画效果 */
@keyframes pulse {
  0% {
    opacity: 0.8;
  }
  50% {
    opacity: 1;
  }
  100% {
    opacity: 0.8;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .footer-wrap {
    padding: 0.75rem;
  }
  
  .site-info {
    flex-direction: column;
    text-align: center;
  }
  
  .stats-container {
    flex-direction: column;
    gap: 1rem;
  }
  
  .stats-section {
    width: 100%;
  }
  
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .footer-wrap {
    padding: 0.6rem 0.5rem;
  }
  
  .site-title {
    font-size: 1rem;
  }
  
  .link-item {
    padding: 0.2rem 0.6rem;
    font-size: 0.7rem;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .stat-item {
    flex-direction: row;
    justify-content: space-between;
    padding: 0.35rem 0.5rem;
  }
}
</style>
