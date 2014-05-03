<?php require('includes/users_connect.php'); 
    $submitted_username = ''; 

    if(!empty($_POST)) { 
        $query = "SELECT password, salt FROM users WHERE username = :username"; 

        $query_params = array( 
            ':username' => $_POST['username'] 
        ); 
          
        try { 
            $stmt = $db->prepare($query); 
            $result = $stmt->execute($query_params); 
        } 
        catch(PDOException $ex){
            die("Failed to run query: " . $ex->getMessage());
        } 

        $login_ok = false; 
        $row = $stmt->fetch(); 

        if ($row) { 
            $check_password = hash('sha256', $_POST['password'] . $row['salt']); 
            
            if($check_password === $row['password']){
                $login_ok = true;
            } 
        } 
 
        if ($login_ok) { 
            unset($row['salt']); 
            unset($row['password']); 
            $_SESSION['username'] = $_POST['username'];  
            header("Location: index.php"); 
            die("Redirecting to: index.php"); 
        } 
        else { 
            ?>
            <script type="text/javascript"> 
                alert("Invalid username or password."); 
                history.back(); 
            </script>
            <?php
            die('Invalid username or password.'); 
            $submitted_username = htmlentities($_POST['username'], ENT_QUOTES, 'UTF-8'); 
        } 
    } 
?> 
