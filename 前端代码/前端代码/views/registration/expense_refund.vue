<template>
  <div>
    <el-divider></el-divider>
    <div style="font-size:20px;text-align:left">
      <i class="el-icon-document-checked">窗口退费</i>
    </div>
    <el-divider></el-divider>
    <h3 align="left">患者信息查询</h3>
    <el-row>
      <el-col :span="8" align="left">
        <div>
          <i class="el-icon-tickets">病历号：</i>
          <el-input style="width:350px" v-model="case_number" placeholder="请输入病历号"></el-input>
        </div>
      </el-col>
      <el-col :span="8" align="left">
        <div>
          <i class="el-icon-user">患者名：</i>
          <el-input style="width:350px" v-model="real_name" placeholder="请输入患者名"></el-input>
        </div>
      </el-col>
      <el-col :span="8" align="left">
        <el-button type="primary" @click="searchPatient()">搜索</el-button>
      </el-col>
    </el-row>
    <el-divider></el-divider>
    <h3 align="left">患者信息确认</h3>
    <el-row>
      <el-col :span="6" align="left">
        <div>
          <i class="el-icon-user">患者名：</i>
          <el-input style="width:200px" disabled v-model="patient.real_name" ></el-input>
        </div>
      </el-col>
      <el-col :span="6" align="left">
        <div>
          <i class="el-icon-postcard">身份证号：</i>
          <el-input style="width:200px" disabled v-model="patient.card_number" ></el-input>
        </div>
      </el-col>
      <el-col :span="6" align="left">
        <div>
          <i class="el-icon-date">年龄：</i>
          <el-input style="width:200px" disabled v-model="patient.age" ></el-input>
        </div>
      </el-col>
      <el-col :span="6" align="left">
        <div>
          <i class="el-icon-male">性别：</i>
          <el-input style="width:100px" disabled v-model="patient.gender" ></el-input>
        </div>
      </el-col>
    </el-row>

    <div align="left" style="margin-top:10px">
      <el-button type="primary" style="width:200px" disabled icon="el-icon-price-tag">项目金额：{{total_price}}元</el-button>
      <el-button type="primary" icon="el-icon-price-tag" @click="expenseCharge()">退费结算</el-button>
    </div>
    <el-table style="margin-top:10px;width:90%" :data="price_project_table" @selection-change="priceSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="item_name" label="项目名称" ></el-table-column>      
      <el-table-column prop="item_price" label="单价" width="80"></el-table-column>
      <el-table-column prop="item_type" label="类型" width="80"></el-table-column>
      <el-table-column prop="item_format" label="规格" width="200"></el-table-column>
      <el-table-column prop="item_number" label="数量" width="80"></el-table-column>
      <el-table-column prop="item_create_time" label="开立时间" width="200"></el-table-column>
    </el-table>
    
  </div>
</template>
<script setup>
import { ref } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import request from "../../util/util"; // 你封装的axios request方法

// 数据
const expense_charge_select = ref([]);
const patient = ref({});
const price_project_table = ref([]);
const total_price = ref(0);
const case_number = ref('');
const real_name = ref('');

// 计算总价
const math_total_price = (total) => {
  if (!total) total = [];
  let sum = 0;
  total.forEach(item => {
    if (!item.item_number) {
      sum += parseFloat(item.item_price);
    } else {
      sum += parseFloat(item.item_price) * parseInt(item.item_number);
    }
  });
  total_price.value = sum.toFixed(2);
}

// 选择行变化
const priceSelectionChange = (val) => {
  expense_charge_select.value = val;
  math_total_price(val);
}

// 搜索患者信息
const searchPatient = async () => {
  try {
    const resp = await request.get(
      `/searchExpenseRefundPatient?case_number=${case_number.value}&real_name=${real_name.value}`
    );
    patient.value = resp.data.registMap || {};
    if (!resp.data.registMap) {
      ElMessage({
        message: "用户不存在，请使用默认病历号 1000009 查询",
        type: "error"
      });
    }
    price_project_table.value = resp.data.requestList || [];
    math_total_price(price_project_table.value);
  } catch (error) {
    console.error(error);
    ElMessage({
      message: "查询患者信息失败",
      type: "error"
    });
  }
}

// 收费 / 退费操作
const expenseCharge = async () => {
  try {
    await ElMessageBox.confirm(
      "本次退费金额为：" + total_price.value,
      "退费窗口",
      {
        confirmButtonText: "退费",
        cancelButtonText: "取消",
      }
    );

    const resp = await request.post("/expenseRefund", expense_charge_select.value);
    ElMessage({
      type: "success",
      message: resp.data.msg,
    });
    searchPatient(); // 刷新数据
  } catch (err) {
    if (err !== "cancel") {
      console.error(err);
      ElMessage({
        type: "info",
        message: "取消收费",
      });
    }
  }
};
</script>


