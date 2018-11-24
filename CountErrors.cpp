#include <iostream>
#include <fstream>
#include<string.h>
using namespace std;

int main()
{
	ifstream ifile_send, ifile_rcv;
	char filename[80];
	char sndfilepath[100]="D:\\socket\\Sender\\",rcvfilepath[100]="D:\\socket\\Receiver\\";
	cin >> filename;
	strcat(sndfilepath,filename);
	strcat(rcvfilepath,filename);
	cout << sndfilepath << endl << rcvfilepath << endl;
	ifile_send.open(sndfilepath);
	ifile_rcv.open(rcvfilepath);
	
	long total = 0, error=0;
	while (!ifile_send.eof() && !ifile_rcv.eof())
	{
		char _send, _rcv;
		_send = ifile_send.get();
		_rcv = ifile_rcv.get();
		total++;
		if (_send != _rcv) error++;
	}
	
	while (!ifile_send.eof())
	{
		char temp;
		temp = ifile_send.get();
		total++;
		error++;
	}
	while (!ifile_rcv.eof())
	{
		char temp;
		temp = ifile_rcv.get();
		total++;
		error++;
	}
	ifile_send.close();
	ifile_rcv.close();
	float rate = (1.0 * error / total) * 100;
	cout << "Error bit :" << 8*error <<"/"<< 8*total << endl << "Error rate  =  "<< rate << "%" <<endl;
	cin.get();
	return 0;
}
