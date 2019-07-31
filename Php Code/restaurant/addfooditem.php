<?php 
session_start();

require_once('../config.php');
mysqli_set_charset($conn, 'utf8mb4');

$response = array();
      
      
        
if(isset($_POST['name']) && isset($_POST['price']) && isset($_POST['description']) && isset($_POST['type']) && isset($_POST['subtype']) && isset($_POST['rid']) ){	

		$name = mysqli_real_escape_string($conn,htmlspecialchars($_POST['name']));
		$price = mysqli_real_escape_string($conn,htmlspecialchars($_POST['price']));
		$description = mysqli_real_escape_string($conn,htmlspecialchars($_POST['description']));
		$type = mysqli_real_escape_string($conn,htmlspecialchars($_POST['type']));
		$subtype = mysqli_real_escape_string($conn,htmlspecialchars($_POST['subtype']));
    $rid = mysqli_real_escape_string($conn,htmlspecialchars($_POST['rid']));
    
        $date = date('Y-m-d H:i:s');
        
        $query_count = "SELECT COUNT(*) 'count' FROM `tbl_food_items`";
        $result_count = mysqli_query($conn, $query_count);
        $row_count = mysqli_fetch_array($result_count);        
        $count = $row_count['count']+1;
        $food_id = 'food_'.$count;
        $query_upload="INSERT INTO `tbl_food_items`(`food_id`, `name`, `price`, `food_desc`, `status`, `typetag`, `subtype_tag`, `recommended`, `image_path`, `sid`, `created_at`) VALUES ('$food_id','$name','$price','$description','Available','$type','$subtype','No','','$rid','$date')";  
        
        if (mysqli_query($conn, $query_upload)) {
        
            array_push($response,
              array('success'=>1,
                  'message'=>"Food item added successfully.",
                  'food_id'=> $food_id
                  ));
    
        } else {
            //echo "Error adding user: " . mysqli_error($conn);
            array_push($response,
                array('success'=>0,
                    'message'=>"Unable to add food item.",
                    ));
        }
    
        
    }
echo json_encode(array('response' => $response));  
mysqli_close($conn);
