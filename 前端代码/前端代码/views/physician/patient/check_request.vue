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
      <i class="el-icon-document-checked">检查申请</i>
    </div>
    <el-divider></el-divider>
    
    <div style="display:flex;">
    <el-button type="primary" disabled icon="el-icon-price-tag">项目金额：{{total_price}}元</el-button>
    </div>
    <el-table :data="check_request_table" style="width:80%" @selection-change="checkSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="tech_code" label="检查编码" width="180"></el-table-column>
        <el-table-column prop="tech_name" label="检查名称" ></el-table-column>
        <el-table-column prop="tech_format" label="检查规格" width="100"></el-table-column>
        <el-table-column prop="tech_price" label="单价" width="100"></el-table-column>
        <el-table-column prop="price_type" label="费用分类" width="100"></el-table-column>
        <el-table-column width="180">
          <template #header>
            <el-button type="text" @click="delCheck()">删除</el-button>
            <el-button type="text" @click="checkDialogTableVisible=true">增加</el-button>
          </template>
        </el-table-column>
    </el-table>
    <el-descriptions title="医嘱" :column="1" border style="width:80%;margin-top:20px">
      <el-descriptions-item label="目的要求：" :labelStyle="{'width':'120px'}">
        <el-input type="textarea" :rows="4" v-model="check_info" placeholder="请输入检验目的要求"></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="检验部位：" :labelStyle="{'width':'120px'}">
        <el-input type="textarea" :rows="2" v-model="check_position" placeholder="请输入检验部位"></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="备注：" :labelStyle="{'width':'120px'}">
        <el-input type="textarea" :rows="4" v-model="check_remark" placeholder="请输入检验事项"></el-input>
      </el-descriptions-item>
    </el-descriptions>
    <!-- 检查项目选择对话框 -->
    <el-dialog title="添加检查申请" v-model="checkDialogTableVisible">
      <div>
        <el-tag style="width:20%">检查编码：</el-tag><el-input style="width:80%" v-model="checkRequest.tech_code" placeholder="请输入检查编码"></el-input>
      </div>
      <div>
        <el-tag style="width:20%">检查名称：</el-tag><el-input style="width:80%" v-model="checkRequest.tech_name" placeholder="请输入检查名称"></el-input>
      </div>
      <div style="float:right">
        <el-button @click="searchCheck()">搜索</el-button>
      </div>
      <el-table :data="search_check" @selection-change="checkSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column property="tech_code" label="检查编码" width="150"></el-table-column>
        <el-table-column property="tech_name" label="检查名称" ></el-table-column>
        <el-table-column prop="tech_format" label="检查规格" width="100"></el-table-column>
        <el-table-column prop="tech_price" label="单价" width="100"></el-table-column>
        <el-table-column prop="price_type" label="费用分类" width="100"></el-table-column>
      </el-table>
      <div>
        <el-button type="primary" @click="addSelectionCheck()">添加</el-button>
        <el-button type="info" @click="checkDialogTableVisible=false">关闭</el-button>
      </div>
    </el-dialog>

    <el-divider></el-divider>
    <div style="text-align:left;margin-top:20px">
        <el-button type="primary" @click="addCheckRequest()">申请提交</el-button>
        <el-button type="primary" @click="clearCheckRequest()">清空表格</el-button>
    </div>
    
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '@/util/util'
import { isNullMessage} from '../../../util/util'
import {ElMessageBox,ElMessage} from 'element-plus'
import qs from 'qs'

// 数据
const submit_check_info = reactive({})
const check_info = ref('')
const check_position = ref('')
const check_remark = ref('')
const total_price = ref(0)
const temp_check = ref([])
const checkDialogTableVisible = ref(false)

const check_request_table = ref([])
const checkRequest = reactive({})
const search_check = ref([])

// 方法
const addCheckRequest = async () => {
  submit_check_info['register_id'] = patient.value.id

  const temp = check_request_table.value.map(item => item.id)
  submit_check_info['medical_technology_ids'] = JSON.stringify(temp)

  submit_check_info['check_info'] = check_info.value
  submit_check_info['check_position'] = check_position.value
  submit_check_info['check_remark'] = check_remark.value

  try {
    const resp = await request.post('/addCheckRequest', qs.stringify(submit_check_info))
    // 假设你全局已经有类似 ElementPlus 的 message 方法
    ElMessage({
      message: resp.data.msg,
      type: 'success'
    })
  } catch (err) {
    console.error(err)
  }
}

const clearCheckRequest = () => {
  check_request_table.value = []
  check_info.value = ''
  check_position.value = ''
  check_remark.value = ''
  total_price.value = 0
}

const checkSelectionChange = (selVal) => {
  temp_check.value = selVal
}

const addSelectionCheck = () => {
  if (temp_check.value?.length) {
    check_request_table.value.push(...temp_check.value)
  }
  // 清空搜索选择
  Object.assign(checkRequest, {});

  search_check.value = []

  math_total_price()
}

const math_total_price = () => {
  let sum = 0
  for (let i = 0; i < check_request_table.value.length; i++) {
    sum += parseFloat(check_request_table.value[i].tech_price)
  }
  total_price.value = sum.toFixed(2)
}

const delCheck = () => {
  temp_check.value.forEach(selItem => {
    for (let j = check_request_table.value.length - 1; j >= 0; j--) {
      if (check_request_table.value[j].tech_name === selItem.tech_name) {
        check_request_table.value.splice(j, 1)
      }
    }
  })
  math_total_price()
}

const searchCheck = async () => {
  try {
    const resp = await request.post('/getCheck', qs.stringify(checkRequest))
    
    search_check.value = resp.data
  } catch (err) {
    console.error(err)
  }
}

///1.定义响应式 patient 对象
const patient = ref({
  id : '',
  real_name: '',
  case_number: '',
  age: '',
  gender: ''
})

//2.页面加载获取患者信息
onMounted(() => {
  const storedPatient = JSON.parse(sessionStorage.getItem('record_now_patient'))
  console.log('storedPatient JSON:', JSON.stringify(storedPatient))
  if (isNullMessage(storedPatient, '请先选择患者')) {
    patient.value = {
      id: '',
      real_name: '',
      case_number: '',
      age: '',
      gender: ''
    }
    
  } else {
    patient.value = storedPatient
    searchCheck()
  }
 
})
</script>

