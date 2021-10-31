#!/bin/python3

import pika


credentials = pika.PlainCredentials('agrossman', 'funky_c4t')
                                    
connection = pika.BlockingConnection(
    pika.ConnectionParameters(host='testservers', credentials=credentials))

channel = connection.channel();

channel.queue_declare(queue='main_send_queue')

channel.basic_publish(exchange='',routing_key='main_send_queue', body = 'test')

connection.close()
