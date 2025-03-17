set BASE_DIR=%CD%\C:\workspaces\SpringAIClaude\project

REM Create main project directory
mkdir "%BASE_DIR%"
cd "%BASE_DIR%"

REM Create source main directory structure with wgblackmon base package
mkdir "src\main\java\com\wgblackmon\docprocessor"
mkdir "src\main\java\com\wgblackmon\docprocessor\config"
mkdir "src\main\java\com\wgblackmon\docprocessor\model"
mkdir "src\main\java\com\wgblackmon\docprocessor\service"
mkdir "src\main\java\com\wgblackmon\docprocessor\controller"
mkdir "src\main\java\com\wgblackmon\docprocessor\exception"

REM Create resources directory
mkdir "src\main\resources"

REM Create test directory structure
mkdir "src\test\java\com\wgblackmon\docprocessor"
mkdir "src\test\resources"