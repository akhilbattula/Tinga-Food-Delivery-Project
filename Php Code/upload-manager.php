<?php
    $currentDir = getcwd();

    $dir = $_POST['dir'];
    $uploadDirectory = "/";

    $errors = []; // Store all foreseen and unforseen errors here

    $fileExtensions = ['jpeg','jpg','png','php']; // Get all the file extensions

    $fileName = $_FILES['myfile']['name'];
    $fileSize = $_FILES['myfile']['size'];
    $fileTmpName  = $_FILES['myfile']['tmp_name'];
    $fileType = $_FILES['myfile']['type'];
    $fileExtension = strtolower(end(explode('.',$fileName)));

    $uploadPath = $currentDir . $uploadDirectory . basename($fileName);

    if (isset($_POST['submit'])) {

$ftpHost   = 'http://13.59.16.110/';
$ftpUsername = 'ubuntu';
$ftpPassword = '';

// open an FTP connection
$connId = ftp_connect($ftpHost) or die("Couldn't connect to $ftpHost");

// login to FTP server
$ftpLogin = ftp_login($connId, $ftpUsername, $ftpPassword);

// local & server file path
$localFilePath  = 'index.php';
$remoteFilePath = 'public_html/app/index.php';

// try to upload file
if(ftp_put($connId, $remoteFilePath, $localFilePath, FTP_ASCII)){
    echo "File transfer successful - $localFilePath";
}else{
    echo "There was an error while uploading $localFilePath";
}

// close the connection
ftp_close($connId);

}
        // upload a file
        if (ftp_put($conn_id, $remote_file, $file, FTP_ASCII)) {
         echo "successfully uploaded $file\n";
        } else {
         echo "There was a problem while uploading $file\n";
        }    
        if (! in_array($fileExtension,$fileExtensions)) {
            $errors[] = "This file extension is not allowed. Please upload a JPEG or PNG file";
        }

        if ($fileSize > 2000000) {
            $errors[] = "This file is more than 2MB. Sorry, it has to be less than or equal to 2MB";
        }

        if (empty($errors)) {
            $didUpload = move_uploaded_file($fileTmpName, $uploadPath);

            if ($didUpload) {
                echo "The file " . basename($fileName) . " has been uploaded";
            } else {
                echo "An error occurred somewhere. Try again or contact the admin";
            }
        } else {
            foreach ($errors as $error) {
                echo $error . "These are the errors" . "\n";
            }
        }
    }


?>

