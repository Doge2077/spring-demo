<template>
    <div style="text-align: center; margin: 0 20px;padding-top: 150px">
        <div>
            <div style="font-size: 25px; font-weight: bold">登录</div>
            <div style="font-size: 14px; color: grey">进入系统前请先输入用户名和密码进行登录</div>
        </div>
        <div style="margin-top: 50px">
            <el-input v-model="form.username" type="text" placeholder="用户名/邮箱">
                <template #prefix>
                    <el-icon>
                        <User/>
                    </el-icon>
                </template>
            </el-input>
            <el-input v-model="form.password" type="password" style="margin-top: 10px" placeholder="密码">
                <template #prefix>
                    <el-icon>
                        <Lock/>
                    </el-icon>
                </template>
            </el-input>
        </div>
        <el-row style="margin-top: 5px">
            <el-col :span="12" style="text-align: left">
                <el-checkbox v-model="form.remember" label="记住我"/>
            </el-col>
            <el-col :span="12" style="text-align: right; margin-top: 5px">
                <el-link @click="forget()">忘记密码</el-link>
            </el-col>
        </el-row>
        <div style="margin-top: 40px">
            <el-button @click="login()" style="width: 270px; box-shadow: 0 0 10px gainsboro" type="primary" plain round>立即登录
            </el-button>
        </div>
        <el-divider class="divider">
            <span style="color: grey; font-size: 12px; margin: 0;">没有账号？注册一个！</span>
        </el-divider>
        <div>
            <el-button @click="register()" style="width: 270px; box-shadow: 0 0 10px gainsboro" type="success" plain round>立即注册
            </el-button>
        </div>
    </div>
</template>

<script setup>
import {User, Lock} from '@element-plus/icons-vue'
import {reactive} from "vue";
import {ElMessage} from "element-plus";
import {get, post} from "@/net";
import router from "@/router";
import {useStore} from "@/stores";

const store = useStore();

const form = reactive({
    username: '',
    password: '',
    remember: false
});

const login = () => {
    if(!form.username || !form.password) {
        ElMessage.warning('请填写用户名和密码！');
    } else {
        post('/api/auth/login', {
            username: form.username,
            password: form.password,
            remember: form.remember
        }, (message) => {
            ElMessage.success(message);
            get('/api/user/me', (message) => {
                store.auth.user = message
                router.push('/index');
            }, () => {
                store.auth.user = null
            });
        });
    }
}

const register = () => {
    router.push("/register");
}

const forget = () => {
    router.push("/forget");
}

</script>

<style scoped>
.divider :deep(.el-divider__text) {
    background: none;
}
</style>