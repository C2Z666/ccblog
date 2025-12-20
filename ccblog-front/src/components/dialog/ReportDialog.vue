<template>
  <!-- 举报 Modal -->
  <el-dialog 
    v-model="reportModal" 
    title="举报"
    width="420px" 
    :border="false"
    :close-on-click-modal="false"
    custom-class="report-dialog"
    append-to-body
  >
    <div class="report-content">
      <div class="report-header">
        <h2 class="report-title">举报此{{ targetTypeName }}</h2>
        <p class="report-subtitle">请选择举报原因</p>
      </div>
      
      <el-form
        ref="formRef"
        :model="reportForm"
        label-position="top"
        label-width="100px"
        class="report-form"
      >
        <!-- 举报类型选择 -->
        <el-form-item
          prop="reportTypeId"
          :rules="[
            {
              required: true,
              message: '请选择举报原因',
              trigger: ['change']
            }
          ]"
          class="report-form-item"
        >
          <el-select 
            v-model="reportForm.reportTypeId" 
            placeholder="请选择举报原因"
            class="report-select"
          >
            <el-option 
              v-for="item in reportTypes" 
              :key="item.id" 
              :label="item.reason" 
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        
        <!-- 补充说明 -->
        <el-form-item
          prop="reasonText"
          class="report-form-item"
        >
          <el-input 
            v-model="reportForm.reasonText" 
            type="textarea" 
            placeholder="请输入补充说明（可选）"
            :rows="4"
            class="report-textarea"
          />
        </el-form-item>
        
        <!-- 提交按钮 -->
        <el-form-item class="report-form-item report-btn-group">
          <el-button 
            type="primary" 
            @click="submitReport"
            :loading="isLoading"
            class="report-button"
            :disabled="reportTypes.length === 0"
          >
            提交举报
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-dialog>
</template>
<script setup lang="ts">
import { reactive, ref, watch, onMounted } from 'vue'
import type { FormInstance } from 'element-plus'
import { doGet, doPost } from '@/http/BackendRequests'
import type { CommonResponse } from '@/http/ResponseTypes/CommonResponseType'
import { REPORT_TYPE_URL, REPORT_URL } from '@/http/URL'
import { messageTip } from '@/util/utils'
import { MESSAGE_TYPE } from '@/constants/MessageTipEnumConstant'

interface ReportTypeVO {
  id: number;
  reason: string;
}

const props = defineProps<{
  modelValue: boolean;
  targetId: number;
  targetType: number;
  targetTypeName: string;
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void
}>()

const reportModal = ref(false)

// 监听父组件传入的modelValue属性变化
watch(() => props.modelValue, (newVal) => {
  reportModal.value = newVal
})

// 监听内部reportModal状态变化，通知父组件
watch(reportModal, (newVal) => {
  emit('update:modelValue', newVal)
  // 如果关闭了对话框，重置表单
  if (!newVal) {
    resetForm()
  }
})

const formRef = ref<FormInstance>()

const reportForm = reactive({
  reportTypeId: 0,
  reasonText: ''
})

const reportTypes = ref<ReportTypeVO[]>([])
const isLoading = ref(false)

// 加载举报类型列表
const loadReportTypes = () => {
  isLoading.value = true
  doGet<CommonResponse>(REPORT_TYPE_URL, {
    type: props.targetType
  })
    .then((response) => {
      if (response.data.status.code === 0) {
        reportTypes.value = response.data.result
        // 如果有举报类型，默认选择第一条
        if (reportTypes.value.length > 0) {
          reportForm.reportTypeId = reportTypes.value[0].id
        }
      } else {
        messageTip(response.data.status.msg || "获取举报类型失败", MESSAGE_TYPE.ERROR)
      }
    })
    .catch((error) => {
      console.error('获取举报类型失败:', error)
      messageTip("网络错误，请稍后重试", MESSAGE_TYPE.ERROR)
    })
    .finally(() => {
      isLoading.value = false
    })
}

// 提交举报
const submitReport = () => {
  if (!formRef.value) return
  
  formRef.value.validate((valid) => {
    if (valid) {
      isLoading.value = true
      
      const reportData = {
        targetType: props.targetType,
        targetId: props.targetId,
        reasonId: reportForm.reportTypeId,
        reasonText: reportForm.reasonText
      }
      
      doPost<CommonResponse>(REPORT_URL, reportData)
        .then((response) => {
          if (response.data.status.code === 0) {
            messageTip("举报提交成功", MESSAGE_TYPE.SUCCESS)
            reportModal.value = false
          } else {
            messageTip(response.data.status.msg || "举报提交失败", MESSAGE_TYPE.ERROR)
          }
        })
        .catch((error) => {
          console.error('举报提交失败:', error)
          messageTip("网络错误，请稍后重试", MESSAGE_TYPE.ERROR)
        })
        .finally(() => {
          isLoading.value = false
        })
    }
  })
}

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  reportForm.reportTypeId = 0
  reportForm.reasonText = ''
}

// 监听举报对话框显示状态，当显示时加载举报类型
watch(reportModal, (newVal) => {
  if (newVal) {
    loadReportTypes()
  }
})

// 监听目标类型变化，重新加载举报类型
watch(() => props.targetType, () => {
  loadReportTypes()
})
</script>

<style scoped>
/* 举报对话框容器 */
.report-dialog {
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

/* 举报内容区域 */
.report-content {
  padding: 30px 0;
  width: 100%;
  display: flex;
  flex-direction: column;
}

/* 举报标题区域 */
.report-header {
  text-align: center;
  margin-bottom: 30px;
  animation: fadeIn 0.5s ease-in-out;
}

.report-title {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

.report-subtitle {
  font-size: 14px;
  color: #666;
  margin: 0;
}

/* 举报表单 */
.report-form {
  width: 100%;
  max-width: 360px;
  margin: 0 auto;
}

.report-form-item {
  margin-bottom: 24px;
  transition: all 0.3s ease;
  animation: slideIn 0.5s ease-out;
  opacity: 0;
  animation-fill-mode: forwards;
}

.report-form-item:nth-child(1) { animation-delay: 0.1s; }
.report-form-item:nth-child(2) { animation-delay: 0.2s; }
.report-form-item:nth-child(3) { animation-delay: 0.3s; }

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 选择框样式 */
.report-select {
  width: 100%;
  height: 44px;
  border-radius: 8px;
  border-color: #e4e7ed;
  transition: all 0.3s ease;
  font-size: 14px;
}

/* 文本域样式 */
.report-textarea {
  width: 100%;
  border-radius: 8px;
  border-color: #e4e7ed;
  transition: all 0.3s ease;
  font-size: 14px;
}

/* 提交按钮组 */
.report-btn-group {
  margin-bottom: 0;
  position: relative;
  z-index: 1;
}

.report-button {
  width: 100%;
  height: 50px;
  border-radius: 12px;
  font-size: 17px;
  font-weight: 600;
  border: none;
  transition: all 0.4s ease;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #ff4d4f, #cf1322);
  color: white;
  box-shadow: 0 4px 20px rgba(255, 77, 79, 0.3);
}

/* 按钮渐变动画 */
.report-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.6s ease;
}

.report-button:hover::before {
  left: 100%;
}

.report-button:hover {
  background: linear-gradient(135deg, #cf1322, #a8071a);
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(255, 77, 79, 0.4);
}

.report-button:active {
  transform: translateY(-1px);
  box-shadow: 0 5px 15px rgba(255, 77, 79, 0.3);
}

/* 加载状态样式 */
:deep(.el-button--primary.is-loading) {
  background: linear-gradient(135deg, #ff7875, #ff4d4f);
  cursor: not-allowed;
  box-shadow: 0 4px 20px rgba(255, 120, 117, 0.3);
}

:deep(.el-button--primary.is-loading:hover) {
  background: linear-gradient(135deg, #ff7875, #ff4d4f);
  transform: none;
  box-shadow: 0 4px 20px rgba(255, 120, 117, 0.3);
}

/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 全局样式覆盖 */
:deep(.el-dialog__headerbtn) {
  top: 20px;
  right: 20px;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

:deep(.el-dialog__headerbtn:hover) {
  background-color: #e4e7ed;
  transform: scale(1.1);
}

:deep(.el-dialog__headerbtn .el-dialog__close) {
  font-size: 18px;
  color: #909399;
  transition: color 0.3s;
  line-height: 1;
}

:deep(.el-dialog__headerbtn .el-dialog__close:hover) {
  color: #606266;
}


/* 响应式设计 */
@media (max-width: 480px) {
  .report-dialog {
    width: 95% !important;
    margin: 0 auto;
    border-radius: 16px;
  }
  
  .report-content {
    padding: 30px 20px;
  }
  
  .report-title {
    font-size: 24px;
  }
  
  .report-form {
    max-width: 100%;
  }
  
  .report-button {
    height: 46px;
  }
}

/* 平滑过渡 */
* {
  transition-property: background-color, border-color, color, fill, stroke, opacity, box-shadow, transform;
}
</style>