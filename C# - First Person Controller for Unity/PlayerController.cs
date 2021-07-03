using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerController : MonoBehaviour
{
    [SerializeField] Transform playerCamera = null;
    [SerializeField] float mouseSensitivity = 3.5f;
    private float movementSpeed = 6.0f;
    [SerializeField] float gravity = -13.0f;
    [SerializeField] [Range(0.0f, 0.5f)] float moveSmoothTime = 0.3f;
    [SerializeField] [Range(0.0f, 0.5f)] float mouseSmoothTime = 0.03f;

    [SerializeField] bool lockCursor = true;

    [SerializeField] private float slopeForce;
    [SerializeField] private float slopeForceRayLength;

    [SerializeField] private float walkSpeed, runSpeed;
    [SerializeField] private float runBuildUp;
    [SerializeField] private KeyCode runKey;

    private float cameraPitch = 0.0f;
    private float velocityY = 0.0f;
    private CharacterController controller = null;

    [SerializeField] private AnimationCurve jumpFallOff;
    [SerializeField] private float jumpMultiplier;
    [SerializeField] private KeyCode jumpKey;

    [SerializeField] private float airSpeed;
    private bool isJumping;
    private bool isGrounded;
    private float playerHeight;

    [SerializeField] private KeyCode crouchKey; 
    [SerializeField] private float crouchSize;
    [SerializeField] private float crouchSpeed;
    [SerializeField] private float crouchJump;
    private bool isCrouching = false;

    private Vector2 currentDir = Vector2.zero;
    private Vector2 currentDirVelocity = Vector2.zero;

    private Vector2 currentMouseDelta = Vector2.zero;
    private Vector2 currentMouseDeltaVelocity = Vector2.zero;

    void Start()
    {
        controller = GetComponent<CharacterController>();
        playerHeight = controller.height;

        if (lockCursor)
        {
            Cursor.lockState = CursorLockMode.Locked;
            Cursor.visible = false;
        }
    }


    void Update()
    {
        isGrounded = Physics.Raycast(transform.position, Vector3.down, playerHeight / 2 + 0.1f);
        
        UpdateMouseLook();
        UpdateMovement();
    }


    void UpdateMouseLook()
    {
        Vector2 targetMouseDelta = new Vector2(Input.GetAxis("Mouse X"), Input.GetAxis("Mouse Y"));

        currentMouseDelta = Vector2.SmoothDamp(currentMouseDelta, targetMouseDelta, ref currentMouseDeltaVelocity, mouseSmoothTime);

        cameraPitch -= currentMouseDelta.y * mouseSensitivity;
        cameraPitch = Mathf.Clamp(cameraPitch, -90.0f, 90.0f);

        playerCamera.localEulerAngles = Vector3.right * cameraPitch;
        transform.Rotate(Vector3.up * currentMouseDelta.x * mouseSensitivity);
    }


    void UpdateMovement()
    {
        Vector2 targetDir = new Vector2(Input.GetAxisRaw("Horizontal"), Input.GetAxisRaw("Vertical"));
        targetDir.Normalize();

        currentDir = Vector2.SmoothDamp(currentDir, targetDir, ref currentDirVelocity, moveSmoothTime);

        if (controller.isGrounded)
        {
            velocityY = 0.0f;
        }

        velocityY += gravity * Time.deltaTime;

        if (isGrounded && !isCrouching)
        {
            Vector3 velocity = (transform.forward * currentDir.y + transform.right * currentDir.x) * movementSpeed + Vector3.up * velocityY;
            controller.Move(velocity * Time.deltaTime);
        }
        else  if (!isGrounded && !isCrouching)
        {
            Vector3 velocity = (transform.forward * currentDir.y + transform.right * currentDir.x) * movementSpeed * airSpeed + Vector3.up * velocityY;
            controller.Move(velocity * Time.deltaTime);
        }
        else if (isCrouching)
        {
            Vector3 velocity = (transform.forward * currentDir.y + transform.right * currentDir.x) * movementSpeed * crouchSpeed + Vector3.up * velocityY;
            controller.Move(velocity * Time.deltaTime);
        }



        if ((Input.GetAxisRaw("Horizontal") != 0 || Input.GetAxisRaw("Vertical") != 0) && OnSlope())
        {
            controller.Move(Vector3.down * controller.height / 2 * slopeForce * Time.deltaTime);
        }

        SetMovementSpeed(); 
        CrouchInput();
        JumpInput();
    }

    private void SetMovementSpeed()
    {
        if (Input.GetKey(runKey) && !isCrouching)
        {
            movementSpeed = Mathf.Lerp(movementSpeed, runSpeed, Time.deltaTime * runBuildUp);
        } 
        else
        {
            movementSpeed = Mathf.Lerp(movementSpeed, walkSpeed, Time.deltaTime * runBuildUp);
        }
    }

    private bool OnSlope()
    {
        if (isJumping)
        {
            return false;
        }

        RaycastHit hit;

        if (Physics.Raycast(transform.position, Vector3.down, out hit, controller.height / 2 * slopeForceRayLength))
        {
            if(hit.normal != Vector3.up)
            {
                return true;
            }  
        }
        return false;
    }

    private void JumpInput()
    {
        if (Input.GetKeyDown(jumpKey) && !isJumping)
        {     
            isJumping = true;
            StartCoroutine(JumpEvent());
        }
    }

    private IEnumerator JumpEvent()
    {
        controller.slopeLimit = 90.0f;
        float timeInAir = 0.0f;

        do
        {
            float jumpForce = jumpFallOff.Evaluate(timeInAir);
            if (!isCrouching) { 
                controller.Move(Vector3.up * jumpForce * jumpMultiplier * Time.deltaTime);
            }
            else if (isCrouching)
            {
                controller.Move(Vector3.up * jumpForce * (jumpMultiplier * crouchJump) * Time.deltaTime);
            }
            timeInAir += Time.deltaTime;
            yield return null;
        } while (!controller.isGrounded && controller.collisionFlags != CollisionFlags.Above);

        controller.slopeLimit = 45.0f;
        isJumping = false;
    }

    private void CrouchInput()
    {
        if (Input.GetKey(crouchKey) && isGrounded && !isJumping)
        {
            isCrouching = true;   
            controller.height = crouchSize;
        }
        else if (Input.GetKeyUp(crouchKey))
        {
            isCrouching = false;
            controller.height = playerHeight;
        }
    }
}