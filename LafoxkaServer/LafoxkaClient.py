#! /usr/bin/python
# coding: utf-8
import socket
import sys
import Skype4Py

HOST = "localhost"
PORT = 4444

# Creating instance of Skype object
skype = Skype4Py.Skype();

# Filter skype id
skypeHandle = ''
counter = 0
for x in sys.argv:
     counter = counter + 1
     if counter > 1:
          skypeHandle = x;

#------------------------------------------------------------------------------------------
# Fired on attachment status change. Here used to re-attach this script to Skype in case attachment is lost.
closed = 0;

def OnAttach(status): 
	print 'API attachment status: ' + skype.Convert.AttachmentStatusToText(status)
	if status == Skype4Py.apiAttachAvailable:
		skype.Attach();

	if status == Skype4Py.apiAttachSuccess:
	   print('******************************************************************************'
); 
	else:
	   global closed
	   closed = 1
	   print 'Skype closed.';
		
#------------------------------------------------------------------------------------------
# Fired on chat message status change. 
# Statuses can be: 'UNKNOWN' 'SENDING' 'SENT' 'RECEIVED' 'READ'		

def OnMessageStatus(Message, Status):
	global skypeHandle
	global skype
	if Status == 'RECEIVED' and (skypeHandle == '' or Message.FromHandle == skypeHandle) and (not Message.FromHandle == skype.CurrentUser.Handle):
		print(Message.FromDisplayName + ': ' + Message.Body)
		try:
			s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
			s.connect((HOST, PORT))
			msg = Message.Body + '\n<![[END]]>\n'
			s.send(msg.encode('utf-8'))
			lafx = s.recv(1024)
			s.close()
			try:
				skype.SendMessage(Message.FromHandle, '/me Lafoxka: ' + lafx.decode('utf-8'));
			except:
				print >>sys.stderr, "Skype message error."
			print 'Lafoxka: ' + lafx;
		except:
			print >>sys.stderr, "Socket error."
#			print sys.exc_info();
		
#	if Status == 'SENT':
#		print('Myself: ' + Message.Body);

			   
#------------------------------------------------------------------------------------------
# assigning handler functions and set application name.

skype.OnAttachmentStatus = OnAttach;
skype.OnMessageStatus = OnMessageStatus;
skype.FriendlyName = 'Lafoxka'

# If Skype is not running, exit.
if not skype.Client.IsRunning:
    print >>sys.stderr, "Skype is not running."
    sys.exit(-1)

print('******************************************************************************'
);
print 'Connecting to Skype..'
skype.Attach();

#------------------------------------------------------------------------------------------
# Looping until user types 'exit' or skype closed
try:
    Cmd = ''
    while not (Cmd == 'exit' or closed == 1):
        Cmd = raw_input('');
except:
    print ''
