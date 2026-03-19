<template>
  <div>
    <el-divider></el-divider>
    <div style="font-size:20px;text-align:left">
      <i class="el-icon-document-checked">窗口退号</i>
    </div>
    <el-divider></el-divider>
    <el-row :gutter="20">
      <el-col :span="6"><el-input v-model="case_number" placeholder="请输入患者病历号"></el-input></el-col>
      <el-col :span="6"><el-input v-model="real_name" placeholder="请输入患者姓名"></el-input></el-col>      
      <el-col :span="2"><el-button @click="getWaitPatient(1)">搜索</el-button></el-col>
      <el-col :span="10"> </el-col>
    </el-row>
    <el-divider></el-divider>
    <el-table
      :data="wait_patient"      style="width: 80%">
      <el-table-column        type="index"     label="编号"        width="80"></el-table-column>
      <el-table-column        prop="case_number"        label="患者病历号">      </el-table-column>
      <el-table-column        prop="real_name"        label="患者姓名"        width="180"></el-table-column>      
      <el-table-column        prop="gender"        label="性别"        width="80"></el-table-column>
      <el-table-column        prop="card_number"        label="身份证号"        width="240"></el-table-column>
      <el-table-column        prop="regist_method"        label="付款方式"        width="180"></el-table-column>
      <el-table-column label="操作">
      <template #default="scope">
        <el-button   size="mini" @click="refundMedicalRecord(scope.$index, scope.row)">退号</el-button>
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
import { ref, onMounted } from "vue";
import request from "../../util/util"; 
import {ElMessage,ElMessageBox} from "element-plus"
// 响应式数据
const case_number = ref('');
const real_name = ref('');
const pageSize = ref(10); // 每页显示行数
const totalCount = ref(0);
const employee_id = ref(1); // 当前医生的id
const wait_patient = ref([]);

// 退号
const refundMedicalRecord = async (scope_index) => {
  const id = wait_patient.value[scope_index].id;
  try {
    await request.get(`/refundMedicalRecord?id=${id}`);
    // 提示信息
    ElMessage({
      message: '退号患者：' + wait_patient.value[scope_index].real_name,
      type: 'success'
    });
    // 刷新等待患者列表
    getWaitPatient(1);
  } catch (error) {
    ElMessage({
      message: '退号失败',
      type: 'error'
    });
  }
};

// 等待患者 table 分页改变时触发
const wait_patient_table_change = (nowPageNumber) => {
  getWaitPatient(nowPageNumber);
};

// 获取等待看诊患者信息
const getWaitPatient = async (nowPageNumber) => {
  try {
    const resp = await request.get(
      `/getRecordRefundPatient?case_number=${case_number.value}&real_name=${real_name.value}&nowPageNumber=${nowPageNumber}&pageSize=${pageSize.value}`
    );
    wait_patient.value = resp.data.list;
    totalCount.value = resp.data.totalCount;
  } catch (error) {
    ElMessage({
      message: '获取等待患者信息失败',
      type: 'error'
    });
  }
};

// 页面挂载时获取数据
onMounted(() => {
  getWaitPatient(1);
});
</script>
