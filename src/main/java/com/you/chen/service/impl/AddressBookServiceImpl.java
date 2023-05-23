package com.you.chen.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.you.chen.mapper.AddressBookMapper;
import com.you.chen.pojo.AddressBook;
import com.you.chen.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper,AddressBook>
     implements AddressBookService {


}
