
# 测试controller
## 测试通过id获取数据

**URL:** `/test/{dataId}`

**Type:** `GET`

**Author:** lipanre

**Content-Type:** `application/x-www-form-urlencoded;charset=UTF-8`

**Description:** 测试通过id获取数据


**Path-parameters:**

| Parameter | Type | Required | Description | Since |
|-----------|------|----------|-------------|-------|
|dataId|string|true|数据id|-|



**Request-example:**
```
curl -X GET -i /test/
```

**Response-example:**
```
string
```

## 查询数据

**URL:** `/test/get`

**Type:** `GET`

**Author:** lipanre

**Content-Type:** `application/json`

**Description:** 查询数据




**Body-parameters:**

| Parameter | Type | Required | Description | Since |
|-----------|------|----------|-------------|-------|
|name|string|false|姓名|-|
|age|int32|false|年龄|-|

**Request-example:**
```
curl -X GET -H 'Content-Type: application/json' -i /test/get --data '{
  "name": "",
  "age": 0
}'
```

**Response-example:**
```
Return void.
```

## 获取用户实体对象

**URL:** `/test`

**Type:** `GET`

**Author:** lipanre

**Content-Type:** `application/x-www-form-urlencoded;charset=UTF-8`

**Description:** 获取用户实体对象





**Request-example:**
```
curl -X GET -i /test
```

**Response-fields:**

| Field | Type | Description | Since |
|-------|------|-------------|-------|
|name|string|用户名|-|
|age|int32|年龄|-|
|sex|enum|性别<br/>(See: 性别枚举)|-|

**Response-example:**
```
{
  "name": "",
  "age": 0,
  "sex": "MAN"
}
```

