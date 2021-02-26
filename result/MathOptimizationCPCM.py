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
unitCost = 0.06
# 首重重量
first_weight_riterion = 1
# 续重重量
continue_weight_riterion = 0.5
# 数据样本的位置
file_path = 'C:/Users/Administrator/Desktop/实验代码/SmartExpress/data/'


# 计算成本
def calculate_cost(table,esid, user_list, es_list):
    total_weight = 0
    total_moving_cost = 0
    for user_index in table:
        total_weight += user_list.loc[user_index]['weight']

        # total_moving_cost += unitCost * Haversine(user_list.loc[user_index]['lon'], user_list.loc[user_index]['lat'],
        #                          es_list.loc[esid]['lon'],
        #                          es_list.loc[esid]['lat'])

    total_moving_cost = calculate_tsp_collect_fee(esid, table, user_list, es_list)

    units = math.ceil(total_weight - first_weight_riterion / continue_weight_riterion)
    charge = es_list.loc[esid]['first_price'] + es_list.loc[esid]['continue_price'] * units
    # 合作成本
    charge += math.log(len(table), 2)
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
    start = datetime.datetime.now()
    # 获取快递点的信息
    es_list = pd.read_table(file_path + 'ES.txt', names=['lon', 'lat', 'first_price', 'continue_price', 'scale'],
                            encoding='utf-8', sep=',', nrows=es_list_size)
    # 1.进行元素的划分
    k = len(es_list)
    iterable = list(range(0, user_list_size))
    # print(iterable)
    res = [p for perm in it.permutations(iterable) for p in mit.partitions(perm) if len(p) == k]
    print(len(res))
    end = datetime.datetime.now()
    # ts = int(time.mktime(time.strptime(str(end - start), "%Y-%m-%d %H:%M:%S")))
    # print('程序的运行时间为:' + str((end - start)))
    # print('程序的运行时间为:' + str((end - start).microseconds))
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

            # print("item:" + str(item))
            if optimal_value > group_cost:
                optimal_value = group_cost
                optimal_list.clear()
                optimal_list.append(item)

        print("最优解:" + str(optimal_value/user_list_size))
        if len(optimal_list) > 0:
            print(optimal_list[0])
            calculate_cost_result(optimal_value, user_list_size, optimal_list[0], user_list, es_list)
        end = datetime.datetime.now()
        # ts = int(time.mktime(time.strptime(str(end - start), "%Y-%m-%d %H:%M:%S")))
        # print('程序的运行时间为:' + str((end - start)))
        print('程序的运行时间为:' + str((end - start).microseconds))


# 计算成本
def calculate_cost_result(optimal_value, user_list_size, users, user_list, es_list):

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
        # 合作成本
        charge += 2
        charge += math.log(len(users[i]), 2)

    print("movingcost:" + str((optimal_value-charge)/user_list_size))
    print("totalcharge:"+str(charge/user_list_size))
    return charge+total_moving_cost

def calculate_tsp_collect_fee(es_no, users, user_list, es_list):
    user_permutations = list(it.permutations(users))
    # 计算tsp移动成本
    move_cost = sys.maxsize
    for i in range(len(user_permutations)):
        move_cost_temp = 0
        user_permutations_list_i = user_permutations[i]
        if len(user_permutations_list_i):
            user_index = user_permutations_list_i[0]
            move_cost_temp += unitCost * Haversine(user_list.loc[user_index]['lon'],
                                                      user_list.loc[user_index]['lat'],
                                                      es_list.loc[es_no]['lon'],
                                                      es_list.loc[es_no]['lat'])
        for j in range(len(user_permutations_list_i)):
            if j == 0:
                user_index = user_permutations_list_i[0]
                move_cost_temp += unitCost * Haversine(user_list.loc[user_index]['lon'],
                                                       user_list.loc[user_index]['lat'],
                                                       es_list.loc[es_no]['lon'],
                                                       es_list.loc[es_no]['lat'])
            if j == (len(user_permutations_list_i) - 1):
                user_index = user_permutations_list_i[len(user_permutations_list_i) - 1]
                move_cost_temp += unitCost * Haversine(user_list.loc[user_index]['lon'],
                                                       user_list.loc[user_index]['lat'],
                                                       es_list.loc[es_no]['lon'],
                                                       es_list.loc[es_no]['lat'])
            else:
                user_index = user_permutations_list_i[j]
                user_index_last = user_permutations_list_i[j-1]
                move_cost_temp += unitCost * Haversine(user_list.loc[user_index]['lon'],
                                                       user_list.loc[user_index]['lat'],
                                                       user_list.loc[user_index_last]['lon'],
                                                       user_list.loc[user_index_last]['lat'])

        if move_cost_temp < move_cost:
            move_cost = move_cost_temp
    return move_cost




if __name__ == "__main__":
    Optimization(6,6)
    # print(list(it.permutations([1, 2, 3])))
