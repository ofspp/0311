<template>
  <div>
    <el-divider></el-divider>
    <div style="font-size:20px;text-align:left">
      <i class="el-icon-document-checked">检验申请</i>
    </div>
    <el-divider></el-divider>
    <el-row :gutter="20">
      <el-col :span="6"><el-input v-model="input_patient_id" placeholder="请输入患者病历号"></el-input></el-col>
      <el-col :span="6"><el-input v-model="input_patient_name" placeholder="请输入患者姓名"></el-input></el-col>      
      <el-col :span="2"><el-button @click="getWaitInspection(1)">搜索</el-button></el-col>
      <el-col :span="10"> </el-col>
    </el-row>
    <el-divider></el-divider>
    <el-table
      :data="wait_inspection"      style="width: 80%">
      <el-table-column        type="index"     label="编号"        width="80"></el-table-column>
      <el-table-column        prop="real_name"        label="患者姓名"        width="180"></el-table-column>
      <el-table-column        prop="case_number"        label="患者病历号">     </el-table-column>
      <el-table-column label="操作">
      <template #default="scope">
        <el-button   size="mini" @click="createInspectionApply(scope.$index, scope.row)">进行检验</el-button>
        <el-button size="mini" >跳过</el-button>
        <el-button size="mini" >叫号</el-button>
      </template>
    </el-table-column>
    </el-table>
    <el-divider></el-divider>
    <el-pagination
    :page-size="pageSize" :total="totalCount" @current-change="wait_patient_table_change"
    layout="prev, pager, next">
  </el-pagination>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import request from '../../util/util'
import { ElMessage } from 'element-plus'

const input_patient_id = ref('')
const input_patient_name = ref('')
const pageSize = ref(5) // 每页显示行数
const totalCount = ref(0)
const finish_inspection_count = ref(0) // 已经检验完成人数
const wait_inspection_count = ref(0)   // 等待检验人数
const wait_inspection = ref([])

// 创建检验
const createInspectionApply = (scope_index, scope_row) => {
  const patient = {
    real_name: wait_inspection.value[scope_index].real_name,
    case_number: wait_inspection.value[scope_index].case_number,
    id: wait_inspection.value[scope_index].id,
    gender: wait_inspection.value[scope_index].gender,
    age: wait_inspection.value[scope_index].age
  }
  sessionStorage.setItem("inspection_now_patient", JSON.stringify(patient))
  ElMessage.success('选择了患者：' + patient.real_name)
 
}

// 等待患者 table 数据改变时触发的事件
const wait_patient_table_change = (nowPageNumber) => {
  getWaitInspection(nowPageNumber)
  console.log("等待患者table数据改变时触发的事件:" + nowPageNumber)
}

// 得到已经检验完成人数
const getFinishInspectionCount = async () => {
  const resp = await request.get('/getFinishInspectionCount')
  finish_inspection_count.value = resp.data
}

// 得到等待检验人数
const getWaitInspectionCount = async () => {
  const resp = await request.get('/getWaitInspectionCount')
  wait_inspection_count.value = resp.data
}

// 得到等待检验患者信息
const getWaitInspection = async (nowPageNumber) => {
  const resp = await request.get('/getWaitInspection', {
    params: {
      case_number: input_patient_id.value,
      real_name: input_patient_name.value,
      nowPageNumber: nowPageNumber,
      pageSize: pageSize.value
    }
  })
  wait_inspection.value = resp.data.list
  totalCount.value = resp.data.totalCount
}

onMounted(() => {
  getFinishInspectionCount()
  getWaitInspectionCount()
  getWaitInspection(1)
})
</script>
