#!/bin/bash

logfile="/opt/backup/log/backup.log"
backupfile="/mnt/backup/backup.tar.gz"
dbexport="/opt/dbexport"
validator="/opt/validator"
htmlpage="/var/www/html/"
hashfile="/mnt/backup/hashfile.sha256"
mountpoint="/mnt/backup/"
current_date="$(date +%c)"

echo "Script started at $current_date" >> $logfile 
if [ $EUID -ne 0 ] 
then
    echo "$current_date ERROR: Entering with not as root" >> $logfile
    echo "You must run that file as a root"
    exit 1;
fi

if ! mountpoint -q /mnt/backup/
then
    echo "$current_date Mounting disk" >> $logfile
    sudo mount /dev/sdb1 /mnt/backup/
    if [ ! $? -eq 0 ]
    then
        echo "$current_date ERROR: Cannot mount disk. Exiting." >> $logfile
        exit 1;
    fi
fi

if [ ! -w "$mountpoint" ]
then
    echo "$current_date ERROR: $mountpoint is not writable. Exiting." >> $logfile
    umount /dev/sdb1
    exit 1
fi

tar -czf $backupfile $dbexport $validator $htmlpage
if [ $? -eq 0 ]
    then    
        hashline=$(sha256sum $backupfile);
        echo "$hashline" >> $hashfile
        echo "$current_date hashline writing: $hashline" >> $logfile
        
        scp $backupfile YOUR_USER@YOUR_DOMAIN:/home/YOUR_USER/backup
        if [ $? -eq 0 ]; 
        then
            echo "$current_date SUCCESS: SCP transfer complete" >> $logfile
            ssh YOUR_USER@YOUR_DOMAIN "echo '$(date): SUCCESS' >> /home/YOUR_USER/backup/file.log"
        else
            echo "$current_date FAILED: SCP transfer failed" >> $logfile
            ssh YOUR_USER@YOUR_DOMAIN "echo '$(date): FAILED' >> /home/YOUR_USER/backup/file.log"
        fi
        umount /dev/sdb1
        echo "$current_date Finishing: SUCCES" >> $logfile
    else
        echo "$current_date ERROR: Someting went wrong during making tar file. Exiting" >> $logfile
        umount /dev/sdb1
    exit 1;
fi
