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
      <i class="el-icon-document-checked">开设处方</i>
    </div>
    <el-divider></el-divider>
    <div style="display:flex">
      <el-button type="primary" disabled icon="el-icon-price-tag">处方金额：{{ total_price }}元</el-button>
    </div>
    <el-table :data="patient_drug_select_table" @selection-change="drugSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column type="expand" width="55">
        <template #default="scope">
          <el-tag style="margin-left:110px">药品编码：{{scope.row.drug_code}}</el-tag>
          <el-tag style="margin-left:10px">包装单位：{{scope.row.drug_unit}}</el-tag>
          <el-tag style="margin-left:10px">生产厂家：{{scope.row.manufacturer}}</el-tag>
          <el-tag style="margin-left:10px">药剂类型：{{scope.row.drug_dosage}}</el-tag>
          <el-tag style="margin-left:10px">药品类型：{{scope.row.drug_type}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="药品名称" prop="drug_name" width="280"></el-table-column>
      <el-table-column label="药品规格" prop="drug_format"  width="160"></el-table-column>
      <el-table-column label="单价" prop="drug_price" width="100"></el-table-column>
      <el-table-column label="用法用量">
        <template #default="scope">
          <el-input type="textarea" :rows="1" v-model="scope.row.drug_usage" placeholder="输入用法用量，使用频次等信息"></el-input>
        </template>
      </el-table-column>
      <el-table-column label="数量" width="200">
        <template #default="scope">
          <el-input-number v-model="scope.row.drug_number" :min="1" :max="100"
            :step="1" step-strictly @change="drugNumberChange"></el-input-number>
        </template>
      </el-table-column>
      <el-table-column width="180">
        <template #header>
          <el-button type="text" @click="delDrugFromTable()">删除</el-button>
          <el-button type="text" @click="drugDialogVisible=true">增加</el-button>
        </template>
      </el-table-column>

    </el-table>
    <div style="text-align:left;margin-top:20px">
      <el-button type="primary" @click="addDrugPrescription()">开立处方</el-button>
      <el-button type="primary" @click="resetDrugInput()">重置处方</el-button>
    </div>

    <!-- 药品选择对话框 -->
    <el-dialog title="添加药品" v-model="drugDialogVisible">
      <div>
        <el-tag style="width:20%">药品名称:</el-tag><el-input style="width:80%" v-model="drug_search.drug_name" placeholder="请输入药品名称（支持模糊查询）"></el-input>
      </div>
      <div>
        <el-tag style="width:20%">拼音助记码:</el-tag><el-input style="width:80%" v-model="drug_search.mnemonic_code" placeholder="请输入药品拼音助记码（支持模糊查询）"></el-input>
      </div>
      <div style="text-align:right">
        <el-button @click="searchDrug()">搜索</el-button>
      </div>
      <el-table :data="view_search_drug_table" @selection-change="drugSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="药品编码" prop="drug_code" width="150"></el-table-column>
        <el-table-column label="药品名称" prop="drug_name"></el-table-column>
        <el-table-column label="药品规格" prop="drug_format" width="100"></el-table-column>
        <el-table-column label="药品单价" prop="drug_price" width="100"></el-table-column>
        <el-table-column label="包装单位" prop="drug_unit" width="100"></el-table-column>
        <el-table-column label="生产厂家" prop="manufacturer" width="150"></el-table-column>
      </el-table>
      <div>
        <el-button type="primary" @click="addSelectionDrug()">添加</el-button>
        <el-button type="info" @click="closeDialog()">关闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import qs from 'qs'

import { isNullMessage} from '@/util/util'
import request from '@/util/util'
// 响应式数据
const submit_prescription = ref([]) // 提交的处方数据
const tempDrug = ref([]) // 复选数据
const drugDialogVisible = ref(false) // 药品选择对话框是否显示
const patient = ref({}) // 患者信息
const total_price = ref('0.00') // 处方金额合计
const patient_drug_select_table = ref([]) // 处方药品列表
const drug_search = reactive({}) // 药品搜索数据
const view_search_drug_table = ref([]) // 显示药品搜索结果

// mounted 生命周期
onMounted(() => {
  patient.value = JSON.parse(sessionStorage.getItem("record_now_patient"))
  if (isNullMessage(patient.value, '请先选择患者')) {
    patient.value = { real_name:'', case_number:'', age:'', gender:'' }
  }
})


// 开设处方
const addDrugPrescription = async () => {
  for (let i = 0; i < patient_drug_select_table.value.length; i++) {
    let temp_pre = {
      register_id: patient.value.id,
      drug_id: patient_drug_select_table.value[i].id,
      drug_usage: patient_drug_select_table.value[i].drug_usage,
      drug_number: patient_drug_select_table.value[i].drug_number
    }
    submit_prescription.value.push(temp_pre)
  }

  const resp = await request.post("/addPrescription", submit_prescription.value)
  if (resp?.msg) {
    alert(resp.msg) // 可以替换为 ElMessage
  }
  patient_drug_select_table.value = []
  math_total_price()
}

// 重置处方输入
const resetDrugInput = () => {
  patient_drug_select_table.value.forEach(drug => {
    drug.drug_number = 1
    drug.drug_usage = ''
  })
}

// 药品数量改变触发
const drugNumberChange = () => {
  math_total_price()
}

// 删除选中药品行
const delDrugFromTable = () => {
  if (!tempDrug.value || tempDrug.value.length === 0) return

  tempDrug.value.forEach(delVal => {
    patient_drug_select_table.value = patient_drug_select_table.value.filter(
      oldVal => oldVal.drug_code !== delVal.drug_code
    )
  })
  math_total_price()
}

// 关闭对话框
const closeDialog = () => {
  clearDialog()
  drugDialogVisible.value = false
}

// 对话框添加药品
const addSelectionDrug = () => {
  if (tempDrug.value && tempDrug.value.length > 0) {
    tempDrug.value.forEach(d => {
      d.drug_number = 1
      patient_drug_select_table.value.push(d)
    })
  }
  clearDialog()
  math_total_price()
  // 关闭对话框
  drugDialogVisible.value = false
}

// 计算处方药品金额
const math_total_price = () => {
  let total = 0
  patient_drug_select_table.value.forEach(d => {
    total += parseFloat(d.drug_price) * parseInt(d.drug_number)
  })
  total_price.value = total.toFixed(2)
}

// 清空对话框数据
const clearDialog = () => {
  for (const key in drug_search) delete drug_search[key]
  view_search_drug_table.value = []
}

// 对话框搜索药品
const searchDrug = async () => {
  const resp = await request.post("/searchDrug", qs.stringify(drug_search))
  view_search_drug_table.value = resp.data 
}

// 复选数据改变
const drugSelectionChange = (val) => {
  tempDrug.value = val
}
</script>
