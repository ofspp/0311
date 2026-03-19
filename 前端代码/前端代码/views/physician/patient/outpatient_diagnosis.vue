<template>
  <div>
    <div style="text-align:left">
      患者信息：
      <el-tag>姓名：{{patient.real_name}}</el-tag>
      <el-tag>病历号：{{patient.case_number}}</el-tag>
      <el-tag>年龄：{{patient.age}}</el-tag>
      <el-tag>性别：{{patient.gender}}</el-tag>
    </div>
    <el-divider></el-divider>
    <div style="font-size:20px;text-align:left">
      <i class="el-icon-document-checked">门诊确诊</i>
    </div>
    <el-divider></el-divider>
    <el-descriptions title="确诊信息录入：" :column="1" border style="width:80%">
      <el-descriptions-item label="诊断结果：" :labelStyle="{'width':'120px'}">
        <el-input type="textarea" :auto-size="{minRows:2,maxRows:4}" v-model="patientDiagnosis.diagnosis" placeholder="输入诊断结果判断"></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="处理意见：" :labelStyle="{'width':'120px'}">
        <el-input type="textarea" :auto-size="{minRows:2,maxRows:4}" v-model="patientDiagnosis.cure" placeholder="输入治疗意见"></el-input>
      </el-descriptions-item>
    </el-descriptions>
    <el-divider></el-divider>
    <div style="text-align:left">
      <el-button type="primary" @click="updatePatientDiagnosis()">确诊提交</el-button> 
      <el-button type="primary" @click="clearPatientDiagnosis()">重置输入</el-button> 
    </div>
  </div>
</template>
<script setup>
import { reactive, ref, onMounted } from 'vue'
import qs from 'qs'
import { isNullMessage} from '@/util/util'
import request from '@/util/util'

const patientDiagnosis = reactive({
  register_id: '', // 患者ID
  diagnosis: '',   // 诊断结果
  cure: ''         // 治疗意见
})

// 提交确诊信息
const updatePatientDiagnosis = async () => {
  if (!patient.value.id) {
    return
  }
  patientDiagnosis.register_id = patient.value.id
  try {
    const resp = await request.post(
      '/outpatientDiagnosis',
      qs.stringify(patientDiagnosis)
    )
    // 提示信息
    messageSuccess(resp.data.msg)
    clearPatientDiagnosis()
  } catch (err) {
    console.error(err)
  }
}

// 重置输入
const clearPatientDiagnosis = () => {
  patientDiagnosis.diagnosis = ''
  patientDiagnosis.cure = ''
}

// 封装提示信息函数（可替换成 ElementPlus 的 this.$message）
const messageSuccess = (msg) => {
  // 这里使用 ElementPlus 的 message
  // import { ElMessage } from 'element-plus'
  // ElMessage({ message: msg, type: 'success' })
  console.log('提示:', msg)
}

//1.定义响应式 patient 对象
const patient = ref({
  real_name: '',
  case_number: '',
  age: '',
  gender: ''
})

//2.页面加载获取患者信息
onMounted(() => {
  
  const storedPatient = JSON.parse(sessionStorage.getItem('record_now_patient'))
 
  if (isNullMessage( storedPatient, '请先选择患者')) {
    patient.value = {
      real_name: '',
      case_number: '',
      age: '',
      gender: ''
    }
  } else {
    patient.value = storedPatient
  }
  
})

</script>
