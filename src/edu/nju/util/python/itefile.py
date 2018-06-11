import os
import collections

def getStmts(path):
    files = os.listdir(path)
    s = []

    for file in files:
        if not os.path.isdir(file):
            f = open(path+"/"+file)
            iter_f = iter(f)
            for line in iter_f:
                s.append(line)

    return s
    # 30539
    # print(len(s))

def getTypes(s):
    types = set()
    for stmt in s:
        type = stmt.split(',')[2]
        types.add(type)
    return types

# dicts of InitializerDeclaration:info InitializerDeclaration:error
def typeWithLevel(s):
    dicts = {}
    for stmt in s:
        level = stmt.split(',')[1]
        type = stmt.split(',')[2]
        if (type,level) in dicts.keys():
            dicts[type,level] = dicts[type,level]+1
        else:
            dicts[type, level] = 1

    return dicts

def getSeq(type,path):
    s = getStmts(path)
    dicts = typeWithLevel(s)
    levelfreqpair = {}
    od = collections.OrderedDict(sorted(dicts.items()))
    for k, v in od.iteritems():
        if type in k[0].lower():
            if k[1].lower() not in levelfreqpair.keys():
                levelfreqpair[k[1].lower()] = v
            else:
                levelfreqpair[k[1].lower()] = levelfreqpair[k[1].lower()] + v
    odlevelfreqpair = sorted(levelfreqpair.items(), key=lambda d:d[1],reverse=True)
    res = []
    for pair in odlevelfreqpair:
        res.append(pair[0][1:])
    return res

def getPercent(type,path):

    s = getStmts(path)
    dicts = typeWithLevel(s)
    levelfreqpair = {}
    od = collections.OrderedDict(sorted(dicts.items()))
    total = 0
    for k, v in od.iteritems():
        if type in k[0].lower():
            if k[1].lower() not in levelfreqpair.keys():
                levelfreqpair[k[1].lower()] = v
            else:
                levelfreqpair[k[1].lower()] = levelfreqpair[k[1].lower()] + v
    odlevelfreqpair = sorted(levelfreqpair.items(), key=lambda d: d[1], reverse=True)

    total = 0.0
    for pair in odlevelfreqpair:
        total = total + pair[1]
    res = []
    print odlevelfreqpair
    for i in range(0,len(odlevelfreqpair)):
        pair = odlevelfreqpair[i]
        print pair
        percent = "%.3f" % (pair[1]/total)
        print percent
        res.append(pair[0][1:]+':'+percent)
    return res




