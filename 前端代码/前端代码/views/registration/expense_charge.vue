<template>
  <div>
    <el-divider></el-divider>
    <div style="font-size:20px;text-align:left">
      <i class="el-icon-document-checked">窗口收费</i>
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
      <el-button type="primary" icon="el-icon-price-tag" @click="expenseCharge()">收费结算</el-button>
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
import { ref } from "vue"
import { ElMessage, ElMessageBox } from "element-plus"
import request from "../../util/util"

// 响应式数据
const expense_charge_select = ref([])
const patient = ref({})
const price_project_table = ref([])
const total_price = ref(0)
const case_number = ref("")
const real_name = ref("")

// 方法：收费
const expenseCharge = () => {
  ElMessageBox.confirm(
    "本次收费金额为：" + total_price.value,
    "收费窗口",
    {
      confirmButtonText: "收费",
      cancelButtonText: "取消"
    }
  )
    .then(() => {
      request.post("/expenseCharge", expense_charge_select.value).then((resp) => {
        ElMessage({
          type: "success",
          message: resp.data.msg
        })
        searchPatient()
      })
    })
    .catch(() => {
      ElMessage({
        type: "info",
        message: "取消收费"
      })
    })
}

// 方法：根据病历号/姓名搜索患者信息
const searchPatient = () => {
  request
    .get("/searchExpenseChargePatient", {
      params: {
        case_number: case_number.value,
        real_name: real_name.value
      }
    })
    .then((resp) => {
      patient.value = resp.data.registMap
      if (!patient.value) {
        ElMessage({
          message: "用户不存在，请使用默认病历号 1000009 查询",
          type: "error"
        })
        patient.value = {}
      }
      price_project_table.value = resp.data.requestList
      math_total_price(price_project_table.value)
    })
}

// 方法：表格选择项发生变化
const priceSelectionChange = (val) => {
  expense_charge_select.value = val
  math_total_price(val)
}

// 方法：计算总金额
const math_total_price = (total) => {
  if (!total) {
    total = []
  }
  total_price.value = 0
  for (let i = 0; i < total.length; i++) {
    if (
      total[i].item_number === undefined ||
      total[i].item_number === null ||
      total[i].item_number === ""
    ) {
      total_price.value += parseFloat(total[i].item_price)
    } else {
      total_price.value +=
        parseFloat(total[i].item_price) * parseInt(total[i].item_number)
    }
  }
  total_price.value = total_price.value.toFixed(2)
}
</script>


