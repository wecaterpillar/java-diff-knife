# 简介
Java数据对比小工具，对比Java对象和集合(List和Tree)的数据差异。

# 特性
- 对比范围 diff scope
  - [x] 简单对象(simple bean)
  - [x] 日期/时间
  - [x] 集合类型(List/Map/Set)
  - [x] 树 Tree
  - [ ] 复杂对象Bean
  - [ ] Json

- 对比识别的操作类型
  - [x] 新增
  - [x] 删除
  - [x] 数据变化
  - [x] 更改位置（同层变化）
  - [x] 移动节点（针对树非同层移动节点）
  
- 对比参数
  - [x] (字段去噪)配置忽略字段（例如系统自动更新字段）
  - [x] (排序去噪)是否忽略排序，以及是否不影响排序结果的排序字段变化
  - [ ] (移动去噪)树对比是否忽略节点移动造成的其他节点变化
  - [x] 支持配置仅对比指定属性字段，默认对比所有非忽略属性
  - [ ] 对比数据层数，全部对比或者对比指定层数
  
- 对比结果 diff result
  - [x] 多种对比类型
  - [x] 支持对象或属性路径
  - [ ] 支持对象或属性标签label
  - [x] 支持变化值的标签(属性关联字典)
  - [ ] 支持忽略某些跟随变化
  - [x] 支持生成变化描述
- 工具使用
  - [x] Util工具类静态方法
    - [ ] 树工具类 TreeUtil.diff(tree1, tree2)
    - [ ] 通用工具类 DiffUtil.diff(obj1, obj2)
  - [ ] 多参数配置

# reference
## open source project
- gojsondiff   
Go JSON Diff   
https://github.com/yudai/gojsondiff


- java-object-diff    
Library to diff and merge Java objects with ease  
https://github.com/SQiShER/java-object-diff   


- fast-object-diff   
Java object diff      
https://github.com/colincatsu/fast-object-diff 


- JaVers  
object auditing and diff framework for Java  
  https://github.com/javers/javers


- hutool   
  🍬A set of tools that keep Java sweet.    
https://github.com/dromara/hutool


- dependency-tree-diff   
An intelligent diff tool for the output of Gradle's dependencies task   
https://github.com/JakeWharton/dependency-tree-diff

## page or article
- 为什么没有一款能称心如意的JSONDIFF工具？  
https://juejin.cn/post/6871984103969964039
- hutool通用树型节点工具类代码介绍  
  https://juejin.cn/post/6927904525731201037
