package com.ikgl.service.impl;

import com.ikgl.pojo.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class testImpl {

    @Autowired
    private UsersServiceImpl usersService;

    /**
     * 1.不要在接口上声明@Transactional ，而要在具体类的方法上使用 @Transactional 注解，否则注解可能无效。
     * 2. 使用了@Transactional的方法，对   同一个类（注意）   里面的方法调用，@Transactional无效。比如有一个类Test，它的一个方法A，A再调用Test本类的方法B
     * （不管B是否public还是private），但A没有声明注解事务，而B有。则外部调用A之后，B的事务是不会起作用的。（经常在这里出错）
       3.使用了@Transactional的方法，只能是public，@Transactional注解的方法都是被外部其他类调用才有效，故只能是public。
       道理和上面的有关联。故在 protected、private 或者 package-visible 的方法上使用 @Transactional 注解，
       它也不会报错，但事务无效。
       4.在action中加上@Transactional，不会回滚。切记不要在action中加上事务。

     *事务传播 propagation(当前就相当于父方法)
     REQUIRED(0),使用当前事务，如果当前没有事务，则自己新建一个任务，子方法是必须运行在一个事务中的，
                 如果存在当前事务，则加入这个事务，成为一个整体。
                 举例：领导没饭吃，我有钱，自己买饭吃。领导有饭吃，我也有饭吃
     SUPPORTS(1),如果当前有事务则使用事务，如果当前没事务，则不使用事务
                 举例：领导没饭吃，我也没饭吃，领导有饭吃，我也有饭吃。
     MANDATORY(2),必须在有transaction状态下执行，如果没有 则会抛出异常 IllegalTransactionStateException
                 举例：领导管饭，不管饭没饭吃，不然不乐意，就不干了（上级一定要有事务不然不执行 用法和REQUIRED一样有异常该方法回滚）
     REQUIRES_NEW(3), （单个用法和REQUIRED一样有异常该方法回滚)
                    1.父方法有事务 子方法出现异常会影响另一个方法的保存
                    2.父方法出现异常 但子方法未出现异常 有设置REQUIRES_NEW的方法会被挂起并执行 没设置的会被回滚
                 如果当前有事务，则挂起该事务，并且创建一个新的事务给自己用，如果当前没事务则同required
     NOT_SUPPORTED(4), 如果当前有事务，则把事务挂起，自己不使用事务去运行数据库操作 和support相反
                    单个使用和父方法有事务一样，NOT_SUPPORTED的方法执行过程中遇到的异常不会滚
     NEVER(5), 调用方不予许有事务的否则会抛异常 使用和NOT_SUPPORTED一样 遇到异常不会滚
     NESTED(6) 如果当前有事务，则开启子事务（嵌套事务） 嵌套事务是独立提交或者回滚；
               如果当前没有事务，则同required.
               但是如果主事务提交了，则会携带子事务一起提交
                如果主事务回滚，则子事务也会一起回滚，相反子事务异常，则父事务可以选择回滚或者不回滚
     */
    //1.user2 user1 没加注解 插入的时候 执行了第一个   第二个抛异常未执行
    //2.在insertTwoTest上加propagation = Propagation.REQUIRED 插入不成功会回滚 两条都回滚
    //3.在子方法里加事务propagation = Propagation.REQUIRED 该方法涉及出错时会回滚回滚
    //@Transactional(propagation = Propagation.MANDATORY)外层必须要有Transactional才不会出错


    //注意点 不能方法同一类去测试 会觉得没有父层级 亲测
   //@Transactional(propagation = Propagation.REQUIRED)
    public void insertTwoTest(){
        usersService.saveUser1(new Users());
        usersService.saveUser2(new Users());

        //int i =1/0;
    }
}
