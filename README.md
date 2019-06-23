# Medibot2

## Interaction

### Create a new alarm

```
U: /newmedi
B: Name for Medi
U: Pill
B: Tell me the time (HH:MM) and interval (default daily, possible: Mon,Tue,Wed,Thu,Fri,Sat,Sun,Daily)
U: 15:30 || 15:40 Mo, Tue, Fri
B: Great! I'll remind you to take your portion of Pill daily. 
```

### Pause an alarm

```
U: /pausemedi
B: Which Medi # Shows selection keyboard
B: I'll not remind you anymore to take Pill
```

### Continue an alarm

```
U: /continuemedi
B: Which Medi # Shows selection keyboard
B: Okay, I'll remind you again to take Pill daily
```

### Delete an alarm

```
U: /deletemedi
B: Which Medi # Shows selection keyboard
B: I'll not remind you anymore to take Pill
```

### Confirming taken

```
B: Take your meds!
U: Taken
```