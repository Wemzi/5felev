from subprocess import PIPE, Popen
import time
import os
import csv
import sys
import platform
from datetime import date
import json

myos = platform.system()
today = date.today().strftime("%Y%m%d")
processes = list()
pings = list()

pingoutput = dict()
pingoutput["date"] = today
pingoutput["system"] = myos

traceoutput = dict()
traceoutput["date"] = today
traceoutput["system"] = myos

with open(sys.argv[1]) as csvfile:
    servers = list(csv.reader(csvfile))
firstones = servers[:10]
lastones = servers[-10:]
selectedones = list()
for idx,server in firstones:
    selectedones.append(server)
for idx,server in lastones:
    selectedones.append(server)


for server in selectedones:
    if len(processes) >= os.cpu_count():
        if processes[0][1].poll() is not None:
            processes.remove(processes[0])
        else:
            processes[0][1].wait()
            tmp = dict()
            tmp["server"] = processes[0][0]
            tmp["output"] = processes[0][1].communicate()[0].decode('ascii')
            pings.append(tmp)
            processes.remove(processes[0])
    p1 = Popen(["ping","-c",'10', server],stdout=PIPE)
    processes.append((server,p1))

for server,process in processes:
    process.wait()
    tmp=dict()
    tmp["server"] = server
    tmp["output"] = process.communicate()[0].decode('ascii')
    pings.append(tmp)

pingoutput["pings"] = pings


traceroutes = list()

for server in selectedones:
    if len(processes) >= os.cpu_count():
        if processes[0][1].poll() is not None:
            processes.remove(processes[0])
        else:
            processes[0][1].wait()
            tmp = dict()
            tmp["server"] = processes[0][0]
            tmp["output"] = processes[0][1].communicate()[0].decode('ascii')
            traceroutes.append(tmp)
            processes.remove(processes[0])
    p1 = Popen(["traceroute","-m",'30', server],stdout=PIPE)
    processes.append((server,p1))


for server,process in processes:
    process.wait()
    tmp = dict()
    tmp["server"] = server
    tmp["output"] = process.communicate()[0].decode('ascii')
    traceroutes.append(tmp)

traceoutput["traces"] = traceroutes
with open("traceroute.json","w+") as outputfile:
    json.dump(traceoutput,outputfile, indent=4,sort_keys=True)

with open("ping.json","w+") as outputfile:
    json.dump(pingoutput,outputfile,indent=4,sort_keys=True)