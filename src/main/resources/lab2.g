expr /"public int h = 1;"/ ["this.h = h + 1;"] = left right;

right /"public int h = 0;"/ ["this.h = h + 1;"] = STICK left right
| XOR left right
| <>;

left /"public int h = 0;"/ ["this.h = h + 1;"] = unary tail;

unary /"public int h = 0;"/ ["this.h = h + 1;"] = NOT unary
| var;

var /"public int h = 0;"/ ["this.h = h + 1;"] = N
| LP expr RP;

tail /"public int h = 0;"/ ["this.h = h + 1;"] = AND unary tail
| <>;

N = "[a-z]";
OR = "\\|\\|";
AND = "&";
STICK = "\\|";
LP = "\\(";
RP = "\\)";
XOR = "\\^";
NOT = "!";