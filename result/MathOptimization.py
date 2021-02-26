from pulp import *
import numpy as np
import datetime
from math import radians, cos, sin, asin, sqrt
import math
import time
import pandas as pd
import itertools as it
import more_itertools as mit

# 单位移动成本
unitCost = 0.02
# 首重重量
first_weight_riterion = 1
# 续重重量
continue_weight_riterion = 0.5
# 数据样本的位置
file_path = 'C:/Users/Administrator/Desktop/SmartExpress/data/'


# 计算成本
def calculate_cost(table,esid, user_list, es_list):
    total_weight = 0
    total_moving_cost = 0
    for user_index in table:
        total_weight += user_list.loc[user_index]['weight']

        total_moving_cost += unitCost * Haversine(user_list.loc[user_index]['lon'], user_list.loc[user_index]['lat'],
                                 es_list.loc[esid]['lon'],
                                 es_list.loc[esid]['lat'])

    units = math.ceil(total_weight - first_weight_riterion / continue_weight_riterion)
    charge = es_list.loc[esid]['first_price'] + es_list.loc[esid]['continue_price'] * units
    # print(str(charge+total_moving_cost))
    return charge+total_moving_cost


def Haversine(lon1, lat1, lon2, lat2):
    # 将十进制度数转化为弧度
    lon1, lat1, lon2, lat2 = map(radians, [lon1, lat1, lon2, lat2])
    radLat1 = radian(lon1)
    radLat2 = radian(lon2)
    a = radian(lon1)-radian(lon2)
    b = radian(lat1)-radian(lat2)
    dist = 2*math.asin(math.sqrt(math.pow(math.sin(a/2),2)+ math.cos(radLat1)*math.cos(radLat2)*math.pow(math.sin(b/2),2)))

    # Haversine公式
    r = 6370996.81  # 地球平均半径，单位为米
    d = dist * r
    return d

def radian(d):
    return d*math.pi/180.0


def Optimization(user_list_size, es_list_size):
    # 获取快递点的信息
    es_list = pd.read_table(file_path + 'ES.txt', names=['lon', 'lat', 'first_price', 'continue_price', 'scale'],
                            encoding='utf-8', sep=',', nrows=es_list_size)
    # 1.进行元素的划分
    k = len(es_list)
    iterable = list(range(0, user_list_size))
    # print(iterable)
    res = [p for perm in it.permutations(iterable) for p in mit.partitions(perm) if len(p) == k]
    print(len(res))
    # 2.加载数据
    for num in range(0, 100):
        print('第' + str(num) + '次结果')
        start = datetime.datetime.now()
        filename = 'large-package-user-' + str(num) + '.txt'
        # print(filename)
        user_list = pd.read_table(file_path + filename, names=['lon', 'lat', 'weight', 'lengthP', 'widthP', 'heightP'],
                                  encoding='utf-8', sep=',', nrows=user_list_size)
        # , skiprows=num * 10
        # print(user_list)
        # 2.寻找最优解
        optimal_value = 100000
        optimal_list = []
        itemindex = 0
        for item in res:
            group_cost = 0
            # print(itemindex)
            itemindex = itemindex + 1
            for index in range(len(item)):
                group_cost += calculate_cost(item[index], index, user_list, es_list)
            if optimal_value > group_cost:
                optimal_value = group_cost
                optimal_list.clear()
                optimal_list.append(item)

        print("最优解:" + str(optimal_value/user_list_size))
        if len(optimal_list) > 0:
            print(optimal_list[0])
            calculate_cost_result(user_list_size, optimal_list[0], user_list, es_list)
        end = datetime.datetime.now()
        # ts = int(time.mktime(time.strptime(str(end - start), "%Y-%m-%d %H:%M:%S")))
        # print('程序的运行时间为:' + str((end - start)))
        print('程序的运行时间为:' + str((end - start).microseconds))




# 计算成本
def calculate_cost_result(user_list_size, users, user_list, es_list):

    total_moving_cost = 0
    charge = 0
    for i in range(len(users)):
        total_weight = 0
        for user_index in users[i]:
            total_weight += user_list.loc[user_index]['weight']
            total_moving_cost += unitCost * Haversine(user_list.loc[user_index]['lon'],
                                                      user_list.loc[user_index]['lat'],
                                                      es_list.loc[i]['lon'],
                                                      es_list.loc[i]['lat'])

        units = math.ceil(total_weight - first_weight_riterion / continue_weight_riterion)
        charge += es_list.loc[i]['first_price'] + es_list.loc[i]['continue_price'] * units

    print("movingcost:" + str(total_moving_cost/user_list_size))
    print("totalcharge:"+str(charge/user_list_size))
    return charge+total_moving_cost

if __name__ == "__main__":
    Optimization(9,3)




