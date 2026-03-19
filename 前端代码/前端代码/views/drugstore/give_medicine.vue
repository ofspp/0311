<template>
  <div>
    <el-divider></el-divider>
    <div style="font-size:20px;text-align:left">
      <i class="el-icon-document-checked">发药</i>
    </div>
    <el-divider></el-divider>

    <el-row :gutter="20">
      <el-col :span="6"><el-input v-model="finish_price_search.case_number" placeholder="请输入患者病历号"></el-input></el-col>
      <el-col :span="6"><el-input v-model="finish_price_search.real_name" placeholder="请输入患者姓名"></el-input></el-col>
      <el-col :span="2"><el-button @click="getFinishPricePatient()">搜索</el-button></el-col>
      <el-col :span="10"> </el-col>
    </el-row>
    <el-table style="margin-top:5px;width:80%" :data="finish_price_patient" size="mini">
      <el-table-column type="index" label="编号" width="80"></el-table-column>
      <el-table-column prop="real_name" label="患者姓名" width="180"></el-table-column>
      <el-table-column prop="case_number" label="患者病历号"></el-table-column>
      <el-table-column prop="age" label="患者年龄" width="180"></el-table-column>
      <el-table-column prop="gender" label="患者性别" width="180"></el-table-column>
      <el-table-column width="120">
        <template #default="scope">
          <el-button type="primary" size="mini" @click="selectPatientDrugs(scope.$index,scope.row)">显示药品</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-top:5px;text-align:right;width:80%">
      <el-pagination :page-size="finish_price_search.pageSize" :total="totalCount" @current-change="finishCheckPatientChange"
      layout="prev,pager,next">
      </el-pagination>
    </div>
    <el-divider></el-divider>

    <el-table :data="finish_check_patient_select" style="width:80%;margin-top:20px;" size="mini">
      <el-table-column type="index" width="50"></el-table-column>
      <el-table-column prop="item_code" label="药品编码"></el-table-column>
      <el-table-column prop="item_name" label="药品名称"></el-table-column>
      <el-table-column prop="item_format" label="药品规格" width="180"></el-table-column>
      <el-table-column prop="item_unit" label="包装单位" width="110"></el-table-column>
      <el-table-column prop="manufacturer" label="生产厂家" width="200"></el-table-column>
      <el-table-column prop="item_price" label="单价" width="120"></el-table-column>
      <el-table-column prop="item_number" label="药品数量" width="120"></el-table-column>
      <el-table-column width="120">
        <template #default="scope">
          <el-button type="primary" size="mini" @click="givePatientDrugs(scope.$index,scope.row)">发药</el-button>
        </template>
      </el-table-column>
    </el-table>

  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/util/util'
import qs from 'qs'

// 响应式数据
const temp_select_patient = ref('') // 记录最后点击的患者

const finish_price_search = reactive({
  case_number: '',
  real_name: '',
  nowPageNumber: 1,
  pageSize: 10, // 每页显示患者数量
})

const update_check_input = reactive({
  check_result: '',
  id: '',
  employee_id: '',
})

const totalCount = ref(0)
const finish_check_patient_select = ref([])
const finish_price_patient = ref([])

// 得到药品已缴费患者
const getFinishPricePatient = async () => {
  try {
    const resp = await request.post(
      '/getFinishPricePatient',
      qs.stringify(finish_price_search)
    )
    finish_price_patient.value = resp.data.list
    totalCount.value = resp.data.totalCount
  } catch (error) {
    ElMessage.error('获取已缴费患者失败')
    console.error(error)
  }
}

// 选择患者，得到该患者缴费的药品信息
const selectPatientDrugs = (index, row) => {
  temp_select_patient.value = row.id
  getPricePatientDrug(row.id)
}

// 获取患者的药品详情
const getPricePatientDrug = async (id) => {
  try {
    const resp = await request.get(`/getPricePatientDrug?id=${id}`)
    finish_check_patient_select.value = resp.data
  } catch (error) {
    ElMessage.error('获取患者药品信息失败')
    console.error(error)
  }
}

// 分页点击
const finishCheckPatientChange = (nowPage) => {
  finish_price_search.nowPageNumber = nowPage
  getFinishPricePatient()
}

// 发药
const givePatientDrugs = async (index, row) => {
  try {
    const resp = await request.post(`/givePatientDrugs?id=${row.id}`)
    ElMessage.success(resp.data.msg)
    getPricePatientDrug(temp_select_patient.value)
  } catch (error) {
    ElMessage.error('发药失败')
    console.error(error)
  }
}

// 生命周期
onMounted(() => {
  getFinishPricePatient()
})
</script>
