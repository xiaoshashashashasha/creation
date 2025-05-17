<script setup>
import {onMounted, ref} from "vue";
import LoginSwitch from "@/components/part/LoginSwitch.vue";
import {useTokenStore} from '@/stores/token'
import {useStateStore} from '@/stores/state'
import {userLoginService, userRegisterService,userCheckRoleService} from "@/api/user";
import {ElMessage} from "element-plus";
import router from "@/router";


const loginMode = ref(true);
const logined = ref(false);

const form_login = ref({
  username: '',
  password: ''
})

const rules_login = {
  username: [{require: true, message: '请输入用户名', trigger: 'blur'},
    {min: 3, max: 12, message: '长度为3到12位', trigger: 'blur'}
  ],
  password: [{require: true, message: '请输入密码', trigger: 'blur'},
    {min: 9, max: 16, message: '长度为9到16位', trigger: 'blur'}
  ]
}

const form_register = ref({
  username: '',
  password: '',
  repassword: '',
  nickname: '',
  gender: '',
  email: ''
})

//密码校验器
const checkRePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次确认密码'))
  } else if (value !== form_register.value.password) {
    callback(new Error('请确保两次输入密码一致'))
  } else {
    callback()
  }
}

const rules_register = {
  username: [{require: true, message: '请输入用户名', trigger: 'blur'},
    {min: 3, max: 12, message: '长度为3到12位', trigger: 'blur'}
  ],
  password: [{require: true, message: '请输入密码', trigger: 'blur'},
    {min: 9, max: 16, message: '长度为9到16位', trigger: 'blur'}
  ],
  nickname: [{require: true, message: '请输入昵称', trigger: 'blur'},
    {min: 1, max: 10, message: '长度为1到10位', trigger: 'blur'}
  ],
  gender: {require: true, message: '请选择性别', trigger: 'blur'},
  email: {require: true, message: '请输入email', trigger: 'blur'},
  repassword: {validator: checkRePassword, trigger: 'blur'}
}


function handleLoginMode(val) {
  if (loginMode.value){
    form_login.value.username = '';
    form_login.value.password = '';
  }else {
    form_register.value.username = '';
    form_register.value.password = '';
    form_register.value.repassword = '';
    form_register.value.nickname = '';
    form_register.value.gender = '';
    form_register.value.email = '';
  }
  loginMode.value = val;
}


const tokenStore = useTokenStore();
const stateStore = useStateStore();

const register = async () => {
  try {
    await userRegisterService(form_register.value);
    ElMessage.success('注册成功')

    form_login.value.username = form_register.value.username;
    form_login.value.password = form_register.value.password;

    loginMode.value = false;
  } catch (err) {
    console.log(err)
  }

}
const login = async () => {
  try {
    let result = await userLoginService(form_login.value);
    ElMessage.success('登录成功')
    //将得到的token存储到pinia中
    tokenStore.setToken(result.data);

    await loginSta()

    logined.value = true;

    router.push('/')
  } catch (err) {
    console.log(err)
  }


}

const loginSta = async () => {
  try {
    const res = await userCheckRoleService()
    if (res.data !== 3) {
      stateStore.setState(res.data)
      router.push('/')
    }else {
      stateStore.setState(3)
    }
  } catch (err) {
    if (err?.response?.status === 401) {
      console.warn('[登录页] token失效')
      stateStore.setState(3)
    } else {
      console.error('其他错误:', err)
      ElMessage.error('系统异常，请稍后再试')
    }
  }
}


onMounted(()=>{
  if (tokenStore.token) {
    loginSta()
  }
})


</script>

<template>
  <body>
  <div :class="{ 'slide-up': logined }" class="slide-container">

  </div>
  <el-row :gutter="0">
    <!-- 表单部分 -->
    <el-col :span="8" class="form-box">
      <div :class="['state', { state1: loginMode, state2: !loginMode }]">
        <LoginSwitch @modeChange="handleLoginMode" style="margin-bottom: 20px; margin-top: 40px; left: 32%"/>

        <transition name="form-fade" mode="out-in">
          <div :key="loginMode">
            <!-- 登录 -->
            <el-form :model="form_login" label-width="auto" class="login-form" v-if="loginMode" :rules="rules_login">
              <el-form-item label="用户名 :" style="margin: 20px; " prop="username">
                <input v-model="form_login.username" style="width: 235px; height: 35px"/>
              </el-form-item>
              <el-form-item label="密码 :" style="margin: 30px 20px" prop="password">
                <input v-model="form_login.password" style="width: 235px; height: 35px"/>
              </el-form-item>

              <div style="text-align: center; margin-top: 20px;">
                <el-button type="primary" @click="login" style="width: 160px; height: 50px; font-size: 26px; font-family: 'Poor Richard';">
                  GoFindBeauty
                </el-button>
              </div>
            </el-form>

            <!-- 注册 -->
            <el-form :model="form_register" label-width="auto" class="login-form" v-else :rules="rules_register">
              <el-form-item label="用户名 :" style="margin: 20px;" prop="username">
                <input v-model="form_register.username" style="width: 235px; height: 35px"/>
              </el-form-item>
              <el-form-item label="昵称 :" style="margin: 30px 20px" prop="nickname">
                <input v-model="form_register.nickname" style="width: 235px; height: 35px"/>
              </el-form-item>
              <el-form-item label="性别 :" style="margin-left: 20px;" prop="gender">
                <el-radio-group v-model="form_register.gender" class="gender-radio-group">
                  <el-radio value="男" style="margin-left: 20px; margin-right: 40px;">男</el-radio>
                  <el-radio value="女">女</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="密码 :" style="margin: 30px 20px" prop="password">
                <input v-model="form_register.password" style="width: 235px; height: 35px"/>
              </el-form-item>
              <el-form-item label="确认密码 :" style="margin: 30px 20px" prop="repassword">
                <input v-model="form_register.repassword" style="width: 235px; height: 35px"/>
              </el-form-item>
              <el-form-item label="邮箱 :" style="margin: 30px 20px" prop="email">
                <input v-model="form_register.email" style="width: 235px; height: 35px"/>
              </el-form-item>

              <div style="text-align: center; margin-top: 20px;">
                <el-button type="primary" @click="register" style="width: 240px; height: 50px; font-size: 26px; font-family: 'Poor Richard';">
                  StartJourneyToBeauty
                </el-button>
              </div>
            </el-form>
          </div>
        </transition>
      </div>
    </el-col>

    <el-col :span="16">
    </el-col>
  </el-row>
  </body>
</template>

<style scoped>
* {
  margin: 0;
  padding: 0;
}

html, body {
  height: 100%;
}


.el-row {
  flex: 1;
  display: flex;
  height: 100%;
  width: 100%;
}

.form-box {
  display: flex;
  justify-content: center;
  padding: 80px 20px 20px;
  height: 100%;
  box-sizing: border-box;
}

.login-form {
  width: 400px;
  padding: 30px 40px;
}

.form-fade-enter-active,
.form-fade-leave-active {
  transition: all 0.6s ease;
}

.form-fade-enter-from,
.form-fade-leave-to {
  opacity: 0;
  transform: scale(0.9);
}

.form-fade-enter-to,
.form-fade-leave-from {
  opacity: 1;
  transform: scale(1);
}

input {
  background-color: transparent;
  border: 1px solid rgba(170, 170, 170, 0.33);
  font-family: 'Poor Richard', serif;
  font-size: 20px;
  color: white;
}

::v-deep(.el-form-item__label) {
  font-family: 'Poor Richard', serif;
  font-size: 26px;
  color: white;
}

::v-deep(.gender-radio-group .el-radio) {
  margin-right: 30px;
  font-size: 20px;
  transform: scale(1.3);
}


.state {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  padding: 20px;
  border-radius: 10px;
  border: 1px solid rgba(255, 255, 255, 0.33);
}


.state1 {
  height: 420px;
  transition: all 1.4s ease;
}

.state2 {
  height: 670px;
  transition: all 0.6s ease;
}
</style>
