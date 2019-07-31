<?php
session_start();
include './config.php';

$response = array();
 
if(isset($_POST['id'])){
    //echo $_POST['id'];
    $date = date('Y-m-d H:i:s');
    $id = $_POST['id'];
    $sql_log = "UPDATE `tbl_users_log` SET `logout_at`='$date' WHERE `id`=$id";
        if (mysqli_query($conn, $sql_log)) {
            session_unset(); 
            session_destroy();            
             array_push($response,
            array('success'=>1,
                'message'=>"Log data recorded Successfully"
                ));
        }else{
            //echo mysqli_error($conn);
            session_unset(); 
            session_destroy();
            $log_status="Failed to log data.";
             array_push($response,
            array('success'=>0,
                'message'=>"Failed to record log data"
                ));
        }
}

echo json_encode(array('response' => $response));  

mysqli_close($conn);