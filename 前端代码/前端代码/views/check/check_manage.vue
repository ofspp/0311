<template>
  <div>
    <el-divider></el-divider>
    <div style="font-size:20px;text-align:left">
      <i class="el-icon-document-checked">检查项目查看</i>
    </div>
    <el-divider></el-divider>

    <el-row :gutter="20">
      <el-col :span="6"><el-input v-model="finish_price_search.case_number" placeholder="请输入患者病历号"></el-input></el-col>
      <el-col :span="6"><el-input v-model="finish_price_search.real_name" placeholder="请输入患者姓名"></el-input></el-col>
      <el-col :span="2"><el-button @click="getPatientQuery()">搜索</el-button></el-col>
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
          <el-button type="primary" size="mini" @click="clickPatient(scope.$index,scope.row)">检查项目</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-top:5px;text-align:right;width:80%">
      <el-pagination :page-size="finish_price_search.pageSize" :total="totalCount" @current-change="getPatientQueryChange"
      layout="prev,pager,next">
      </el-pagination>
    </div>
    <el-divider></el-divider>

    <el-table style="margin-top:10px;width:80%" :data="price_project_table">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="item_name" label="项目名称" ></el-table-column>      
      <el-table-column prop="item_price" label="单价" width="80"></el-table-column>
      <el-table-column prop="item_type" label="类型" width="80"></el-table-column>
      <el-table-column prop="item_code" label="项目编码" width="200"></el-table-column>
      <el-table-column prop="item_state" label="状态" width="80"></el-table-column>
      <el-table-column prop="item_create_time" label="开立时间" width="200"></el-table-column>
    </el-table>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import qs from 'qs'
import request from '@/util/util' // 根据你的封装路径调整

// -------------------- 数据 --------------------
const temp_select_patient = ref('') // 记录最后点击的患者
const totalCount = ref(0)
const price_project_table = ref([])
const finish_price_patient = ref([])

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

// -------------------- 方法 --------------------

// 得到患者
const getPatientQuery = async () => {
  try {
    const resp = await request.post(
      '/getPatientQuery',
      qs.stringify(finish_price_search)
    )
    finish_price_patient.value = resp.data.list
    totalCount.value = resp.data.totalCount
  } catch (error) {
    console.error('获取患者信息失败:', error)
  }
}

// 选择患者，得到该患者缴费的药品信息
const clickPatient = (index, row) => {
  temp_select_patient.value = row.id
  getPatientItemByPhysician(row.id)
}

const getPatientItemByPhysician = async (id) => {
  try {
    const resp = await request.get(`/getPatientItemByCheck?register_id=${id}`)
    price_project_table.value = resp.data.list
  } catch (error) {
    console.error('获取患者缴费药品信息失败:', error)
  }
}

// 分页点击
const getPatientQueryChange = (nowPage) => {
  finish_price_search.nowPageNumber = nowPage
  getPatientQuery()
}

// -------------------- 生命周期 --------------------
onMounted(() => {
  getPatientQuery()
})
</script>
