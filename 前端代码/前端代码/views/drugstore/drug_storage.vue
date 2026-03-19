<template>
  <div>
    <el-divider></el-divider>
    <div style="font-size:20px;text-align:left">
      <i class="el-icon-document-checked">药库管理</i>
    </div>
    <el-divider></el-divider>

    <el-row :gutter="20">
      <el-col :span="6"><el-input v-model="finish_price_search.drug_code" placeholder="请输入药品编号"></el-input></el-col>
      <el-col :span="6"><el-input v-model="finish_price_search.mnemonic_code" placeholder="请输入拼音助记码"></el-input></el-col>
      <el-col :span="6"><el-input v-model="finish_price_search.drug_name" placeholder="请输入药品名称"></el-input></el-col>
      <el-col :span="2"><el-button @click="getDrugInfo()">搜索</el-button></el-col>
      <el-col :span="4"> </el-col>
    </el-row>
    <el-divider></el-divider>
      <div style="font-size:20px;text-align:left">
        <el-button icon="el-icon-document-checked" type="primary" @click="addDrugs()">增加药品</el-button>
      </div>
    <el-divider></el-divider>
    <el-table :data="finish_price_patient" style="width:80%;margin-top:20px;" size="mini">
      <el-table-column type="index" width="50"></el-table-column>
      <el-table-column prop="drug_code" label="药品编码"></el-table-column>
      <el-table-column prop="drug_name" label="药品名称"></el-table-column>
      <el-table-column prop="drug_format" label="药品规格" width="180"></el-table-column>
      <el-table-column prop="drug_unit" label="包装单位" width="110"></el-table-column>
      <el-table-column prop="manufacturer" label="生产厂家" width="180"></el-table-column>
      <el-table-column prop="drug_price" label="单价" width="120"></el-table-column>
      <el-table-column width="120">
        <template #default="scope">
          <el-button type="text" size="mini" @click="editDrugs(scope.$index,scope.row)">修改</el-button>
          <el-button type="text" size="mini" @click="delDrugs(scope.$index,scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-top:5px;text-align:right;width:80%">
      <el-pagination :page-size="finish_price_search.pageSize" :total="totalCount" @current-change="finishCheckPatientChange"
      layout="prev,pager,next">
      </el-pagination>
    </div>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '修改药品' : '新增药品'" width="600px">
      <el-form ref="drugFormRef" :model="drugForm" :rules="formRules" label-width="100px">
        <el-form-item label="药品编码" prop="drug_code">
          <el-input v-model="drugForm.drug_code" placeholder="请输入药品编码"></el-input>
        </el-form-item>
        <el-form-item label="药品名称" prop="drug_name">
          <el-input v-model="drugForm.drug_name" placeholder="请输入药品名称"></el-input>
        </el-form-item>
        <el-form-item label="药品规格" prop="drug_format">
          <el-input v-model="drugForm.drug_format" placeholder="请输入药品规格"></el-input>
        </el-form-item>
        <el-form-item label="包装单位" prop="drug_unit">
          <el-input v-model="drugForm.drug_unit" placeholder="请输入包装单位"></el-input>
        </el-form-item>
        <el-form-item label="生产厂家" prop="manufacturer">
          <el-input v-model="drugForm.manufacturer" placeholder="请输入生产厂家"></el-input>
        </el-form-item>
        <el-form-item label="药剂类型" prop="drug_dosage">
          <el-input v-model="drugForm.drug_dosage" placeholder="如 胶囊剂/片剂"></el-input>
        </el-form-item>
        <el-form-item label="药品类型" prop="drug_type">
          <el-input v-model="drugForm.drug_type" placeholder="如 处方药/西药"></el-input>
        </el-form-item>
        <el-form-item label="单价" prop="drug_price">
          <el-input v-model.number="drugForm.drug_price" placeholder="请输入单价"></el-input>
        </el-form-item>
        <el-form-item label="助记码" prop="mnemonic_code">
          <el-input v-model="drugForm.mnemonic_code" placeholder="请输入拼音助记码"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitDrug">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted, getCurrentInstance } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/util/util'   // 这里用你封装好的 axios 实例
import qs from 'qs'

// 获取组件实例用于表单验证
const { proxy } = getCurrentInstance()

// 响应式数据
const temp_select_patient = ref('') // 记录最后点击的患者
const finish_price_search = reactive({
  drug_code: '',
  mnemonic_code: '',
  drug_name: '',
  nowPageNumber: 1,
  pageSize: 10, // 每页显示数量
})

const update_check_input = reactive({
  check_result: '',
  id: '',
  employee_id: '',
})

const totalCount = ref(0)
const finish_check_patient_select = ref([])
const finish_price_patient = ref([])

// 弹窗与表单
const dialogVisible = ref(false)
const isEdit = ref(false)
const drugFormRef = ref(null)
const drugForm = reactive({
  id: '',
  drug_code: '',
  drug_name: '',
  drug_format: '',
  drug_unit: '',
  manufacturer: '',
  drug_dosage: '',
  drug_type: '',
  drug_price: '',
  mnemonic_code: ''
})

// 表单验证规则
const formRules = ref({
  drug_code: [
    { required: true, message: '请输入药品编码', trigger: 'blur' }
  ],
  drug_name: [
    { required: true, message: '请输入药品名称', trigger: 'blur' }
  ],
  drug_format: [
    { required: true, message: '请输入药品规格', trigger: 'blur' }
  ],
  drug_unit: [
    { required: true, message: '请输入包装单位', trigger: 'blur' }
  ],
  manufacturer: [
    { required: true, message: '请输入生产厂家', trigger: 'blur' }
  ],
  drug_price: [
    { required: true, message: '请输入单价', trigger: 'blur' },
    { type: 'number', message: '单价必须为数字', trigger: 'blur' },
    { validator: (rule, value, callback) => {
      if (value < 0) {
        callback(new Error('单价不能为负数'))
      } else {
        callback()
      }
    }, trigger: 'blur' }
  ]
})

const openAddDialog = () => {
  isEdit.value = false
  // 重置表单
  if (drugFormRef.value) {
    drugFormRef.value.resetFields()
  }
  Object.assign(drugForm, {
    id: '',
    drug_code: '',
    drug_name: '',
    drug_format: '',
    drug_unit: '',
    manufacturer: '',
    drug_dosage: '',
    drug_type: '',
    drug_price: '',
    mnemonic_code: ''
  })
  dialogVisible.value = true
}

// 得到药品
const getDrugInfo = async () => {
  try {
    const resp = await request.post(
      '/getDrugInfo',
      qs.stringify(finish_price_search)
    )
    finish_price_patient.value = resp.data.list || []
    totalCount.value = resp.data.totalCount || 0
  } catch (error) {
    ElMessage.error('获取药品信息失败')
    console.error(error)
  }
}

// 分页点击
const finishCheckPatientChange = (nowPage) => {
  finish_price_search.nowPageNumber = nowPage
  getDrugInfo()
}

// 修改
const editDrugs = (index, row) => {
  isEdit.value = true
  // 重置表单验证状态
  if (drugFormRef.value) {
    drugFormRef.value.clearValidate()
  }
  // 复制行数据到表单
  Object.assign(drugForm, { ...row })
  dialogVisible.value = true
}

// 删除
const delDrugs = (index, row) => {
  ElMessageBox.confirm(`确认删除【${row.drug_name}】吗？`, '提示', { 
    type: 'warning',
    confirmButtonText: '确认',
    cancelButtonText: '取消'
  })
  .then(async () => {
    try {
      // 显示加载中提示
      ElMessage({
        message: '正在删除...',
        type: 'info',
        duration: 0,
        showClose: true
      })
      
      const resp = await request.post('/deleteDrug', qs.stringify({ id: row.id }))
      
      // 关闭加载提示
      proxy.$message.closeAll()
      
      if (resp.data.status === 'success') {
        ElMessage.success(resp.data.msg || '删除成功')
        getDrugInfo() // 重新加载数据
      } else {
        ElMessage.error(resp.data.msg || '删除失败')
      }
    } catch (e) {
      proxy.$message.closeAll()
      ElMessage.error('删除失败')
      console.error(e)
    }
  })
  .catch(() => {
    // 用户取消删除
    ElMessage.info('已取消删除')
  })
}

// 增加
const addDrugs = () => {
  openAddDialog()
}

// 提交保存
const submitDrug = async () => {
  try {
    // 表单验证
    await drugFormRef.value.validate()
    
    // 显示加载中提示
    ElMessage({
      message: isEdit.value ? '正在修改...' : '正在添加...',
      type: 'info',
      duration: 0,
      showClose: true
    })
    
    const url = isEdit.value ? '/updateDrug' : '/addDrug'
    const resp = await request.post(url, qs.stringify(drugForm))
    
    // 关闭加载提示
    proxy.$message.closeAll()
    
    if (resp.data.status === 'success') {
      ElMessage.success(resp.data.msg || '操作成功')
      dialogVisible.value = false
      getDrugInfo() // 重新加载数据
    } else {
      ElMessage.error(resp.data.msg || '操作失败')
    }
  } catch (error) {
    // 关闭加载提示
    if (proxy) proxy.$message.closeAll()
    
    // 表单验证失败
    if (error.name === 'ValidationError') {
      return
    }
    
    ElMessage.error('提交失败')
    console.error(error)
  }
}

// 生命周期
onMounted(() => {
  getDrugInfo()
})
</script>
